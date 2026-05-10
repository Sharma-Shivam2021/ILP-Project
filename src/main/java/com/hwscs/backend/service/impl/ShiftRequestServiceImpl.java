package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.request.CreateShiftRequestDto;
import com.hwscs.backend.dto.request.InchargeApprovalDto;
import com.hwscs.backend.dto.response.PeerResponseDto;
import com.hwscs.backend.dto.response.ShiftRequestResponseDto;
import com.hwscs.backend.entity.*;
import com.hwscs.backend.entity.enums.RequestAction;
import com.hwscs.backend.entity.enums.RequestStatus;
import com.hwscs.backend.exception.InvalidRequestException;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.exception.UnauthorizedActionException;
import com.hwscs.backend.repository.*;
import com.hwscs.backend.service.ShiftRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftRequestServiceImpl implements ShiftRequestService {

    private final ShiftRequestRepository shiftRequestRepository;
    private final NurseRepository nurseRepository;
    private final NurseShiftRepository nurseShiftRepository;
    private final RequestHistoryRepository requestHistoryRepository;
    private final NursingInchargeRepository nursingInchargeRepository;

    // ── Create ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ShiftRequestResponseDto createRequest(CreateShiftRequestDto dto, String requesterUsername) {

        Nurse requester = getNurseByUsername(requesterUsername);
        Nurse peer = nurseRepository.findById(dto.getPeerNurseId())
                .orElseThrow(() -> new ResourceNotFoundException("Peer nurse not found"));

        // Validation: nurse cannot request a swap with themselves
        if (requester.getId().equals(peer.getId())) {
            throw new InvalidRequestException("A nurse cannot request a shift swap with themselves");
        }

        // Validation: cross-department swaps not allowed
        if (!requester.getDepartment().getId().equals(peer.getDepartment().getId())) {
            throw new InvalidRequestException("Shift swaps are only allowed within the same department");
        }

        NurseShift requesterShift = nurseShiftRepository.findById(dto.getRequesterShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Requester shift not found"));

        NurseShift peerShift = nurseShiftRepository.findById(dto.getPeerShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Peer shift not found"));

        // Validation: only future shifts can be swapped
        if (!requesterShift.getShiftDate().isAfter(LocalDate.now())) {
            throw new InvalidRequestException("Only future shifts can be swapped");
        }
        if (!peerShift.getShiftDate().isAfter(LocalDate.now())) {
            throw new InvalidRequestException("Only future shifts can be swapped");
        }

        // Validation: shifts must actually belong to the respective nurses
        if (!requesterShift.getNurse().getId().equals(requester.getId())) {
            throw new UnauthorizedActionException("Shift does not belong to the requesting nurse");
        }
        if (!peerShift.getNurse().getId().equals(peer.getId())) {
            throw new InvalidRequestException("Peer shift does not belong to the peer nurse");
        }

        ShiftRequest request = ShiftRequest.builder()
                .requesterNurse(requester)
                .peerNurse(peer)
                .requesterNurseShift(requesterShift)
                .peerNurseShift(peerShift)
                .status(RequestStatus.PENDING_PEER)
                .remarks(dto.getRemarks())
                .build();

        ShiftRequest saved = shiftRequestRepository.save(request);
        saveHistory(saved, requester.getUser(), RequestAction.CREATED, "Shift swap request created");

        return mapToDto(saved);
    }

    // ── Peer Response ──────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ShiftRequestResponseDto peerResponse(PeerResponseDto dto, String peerUsername) {

        ShiftRequest request = getRequestById_entity(dto.getShiftRequestId());

        // Only the actual peer nurse can respond
        Nurse peer = getNurseByUsername(peerUsername);
        if (!request.getPeerNurse().getId().equals(peer.getId())) {
            throw new UnauthorizedActionException("Only the peer nurse can respond to this request");
        }

        // Can only respond when request is in PENDING_PEER state
        if (request.getStatus() != RequestStatus.PENDING_PEER) {
            throw new InvalidRequestException("This request is not pending peer response. Current status: " + request.getStatus());
        }

        if (dto.isAccepted()) {
            request.setStatus(RequestStatus.PEER_ACCEPTED);
            saveHistory(request, peer.getUser(), RequestAction.PEER_ACCEPTED, dto.getRemarks());
        } else {
            request.setStatus(RequestStatus.PEER_REJECTED);
            saveHistory(request, peer.getUser(), RequestAction.PEER_REJECTED, dto.getRemarks());
        }

        return mapToDto(shiftRequestRepository.save(request));
    }

    // ── Incharge Review ────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ShiftRequestResponseDto inchargeReview(InchargeApprovalDto dto, String inchargeUsername) {

        ShiftRequest request = getRequestById_entity(dto.getShiftRequestId());

        // Incharge can only approve requests in PEER_ACCEPTED state
        if (request.getStatus() != RequestStatus.PEER_ACCEPTED) {
            throw new InvalidRequestException(
                    "Only peer-accepted requests can be reviewed. Current status: " + request.getStatus()
            );
        }

        // Department check: incharge can only manage their own department
        NursingIncharge incharge = nursingInchargeRepository.findByUser_Username(inchargeUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Nursing incharge profile not found"));

        if (!incharge.getDepartment().getId()
                .equals(request.getRequesterNurse().getDepartment().getId())) {
            throw new UnauthorizedActionException("You can only manage shift requests for your own department");
        }

        if (dto.isApproved()) {
            request.setStatus(RequestStatus.APPROVED);
            saveHistory(request, incharge.getUser(), RequestAction.APPROVED, dto.getRemarks());

            // Execute the shift swap: swap the Shift references between the two NurseShift records
            executeShiftSwap(request);

        } else {
            request.setStatus(RequestStatus.REJECTED);
            saveHistory(request, incharge.getUser(), RequestAction.REJECTED, dto.getRemarks());
        }

        return mapToDto(shiftRequestRepository.save(request));
    }

    // ── Queries ────────────────────────────────────────────────────────────────

    @Override
    public List<ShiftRequestResponseDto> getMyRequests(String nurseUsername) {
        Nurse nurse = getNurseByUsername(nurseUsername);
        return shiftRequestRepository.findAllInvolvingNurse(nurse)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ShiftRequestResponseDto> getPendingInchargeReview(String inchargeUsername) {
        NursingIncharge incharge = nursingInchargeRepository.findByUser_Username(inchargeUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Nursing incharge profile not found"));

        return shiftRequestRepository.findByDepartmentAndStatus(
                        incharge.getDepartment(), RequestStatus.PEER_ACCEPTED)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ShiftRequestResponseDto getRequestById(Integer requestId) {
        return mapToDto(getRequestById_entity(requestId));
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    private void executeShiftSwap(ShiftRequest request) {
        NurseShift requesterShift = request.getRequesterNurseShift();
        NurseShift peerShift = request.getPeerNurseShift();

        // Swap the Shift (timing/name) between the two NurseShift assignments
        Shift tempShift = requesterShift.getShift();
        requesterShift.setShift(peerShift.getShift());
        peerShift.setShift(tempShift);

        requesterShift.setIsSwapped(true);
        peerShift.setIsSwapped(true);

        nurseShiftRepository.save(requesterShift);
        nurseShiftRepository.save(peerShift);
    }

    private void saveHistory(ShiftRequest request, User actor, RequestAction action, String remarks) {
        RequestHistory history = RequestHistory.builder()
                .shiftRequest(request)
                .actorUser(actor)
                .action(action)
                .remarks(remarks)
                .build();
        requestHistoryRepository.save(history);
    }

    private Nurse getNurseByUsername(String username) {
        return nurseRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse profile not found for user: " + username));
    }

    private ShiftRequest getRequestById_entity(Integer id) {
        return shiftRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift request not found: " + id));
    }

    public ShiftRequestResponseDto mapToDto(ShiftRequest r) {
        NurseShift rs = r.getRequesterNurseShift();
        NurseShift ps = r.getPeerNurseShift();

        return ShiftRequestResponseDto.builder()
                .id(r.getId())
                .status(r.getStatus().name())
                .remarks(r.getRemarks())
                .createdAt(r.getCreatedAt())
                // Requester
                .requesterNurseId(r.getRequesterNurse().getId())
                .requesterNurseName(r.getRequesterNurse().getFullName())
                .requesterShiftDate(rs.getShiftDate())
                .requesterShiftName(rs.getShift().getShiftName())
                .requesterShiftStart(rs.getShift().getStartTime())
                .requesterShiftEnd(rs.getShift().getEndTime())
                // Peer
                .peerNurseId(r.getPeerNurse().getId())
                .peerNurseName(r.getPeerNurse().getFullName())
                .peerShiftDate(ps.getShiftDate())
                .peerShiftName(ps.getShift().getShiftName())
                .peerShiftStart(ps.getShift().getStartTime())
                .peerShiftEnd(ps.getShift().getEndTime())
                .build();
    }
}

package com.hwscs.backend.service.interfaces;

import com.hwscs.backend.dto.requests.CreateShiftRequestDto;
import com.hwscs.backend.dto.requests.InchargeApprovalDto;
import com.hwscs.backend.dto.response.EligiblePeerDto;
import com.hwscs.backend.dto.response.PeerResponseDto;
import com.hwscs.backend.dto.response.RequestHistoryResponseDto;
import com.hwscs.backend.dto.response.ShiftRequestResponseDto;

import java.util.List;

public interface ShiftRequestService {
    ShiftRequestResponseDto createRequest(CreateShiftRequestDto dto, String requesterUsername);

    ShiftRequestResponseDto peerResponse(PeerResponseDto dto, String peerUsername);

    ShiftRequestResponseDto inchargeReview(InchargeApprovalDto dto, String inchargeUsername);

    // Nurse: view all requests they are involved in
    List<ShiftRequestResponseDto> getMyRequests(String nurseUsername);

    // Nursing Incharge: view all PEER_ACCEPTED requests in their department
    List<ShiftRequestResponseDto> getPendingInchargeReview(String inchargeUsername);

    // View a single request by ID
    ShiftRequestResponseDto getRequestById(Integer requestId);

    // List all eligible peer nurses for shift
    List<EligiblePeerDto> getEligiblePeers(
            Integer requesterShiftId,
            String requesterUsername
    );

    // Cancel Request
    ShiftRequestResponseDto cancelRequest(Integer requestId, String requestUsername);

    // Get Request History
    List<RequestHistoryResponseDto> getRequestHistory(
            Integer requestId,
            String username
    );
}
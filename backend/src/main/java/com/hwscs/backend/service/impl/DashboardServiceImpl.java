package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.response.*;
import com.hwscs.backend.entity.*;
import com.hwscs.backend.enums.RequestStatus;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.repository.*;
import com.hwscs.backend.service.interfaces.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final NurseRepository nurseRepository;
    private final NurseShiftRepository nurseShiftRepository;
    private final ShiftRequestRepository shiftRequestRepository;
    private final NursingInchargeRepository nursingInchargeRepository;
    private final DutyOfficerRepository dutyOfficerRepository;
    private final NurseServiceImpl nurseServiceImpl;
    private final ShiftRequestServiceImpl shiftRequestServiceImpl;

    public DashboardServiceImpl(NurseRepository nurseRepository, NurseShiftRepository nurseShiftRepository,
                                ShiftRequestRepository shiftRequestRepository, NurseServiceImpl nurseServiceImpl,
                                ShiftRequestServiceImpl shiftRequestServiceImpl, NursingInchargeRepository nursingInchargeRepository, DutyOfficerRepository dutyOfficerRepository) {
        super();
        this.nurseRepository = nurseRepository;
        this.nurseShiftRepository = nurseShiftRepository;
        this.shiftRequestRepository = shiftRequestRepository;
        this.nurseServiceImpl = nurseServiceImpl;
        this.shiftRequestServiceImpl = shiftRequestServiceImpl;
        this.nursingInchargeRepository = nursingInchargeRepository;
        this.dutyOfficerRepository = dutyOfficerRepository;
    }

    @Override
    public NurseDashboardDto getNurseDashboard(String username) {
        Nurse nurse = nurseRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse profile not found"));

        List<NurseShift> upcomingShifts = nurseShiftRepository.findByNurse(nurse).stream()
                .filter(s -> !s.getShiftDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(NurseShift::getShiftDate)).toList();

        List<ShiftRequest> requests = shiftRequestRepository.findAllInvolvingNurse(nurse);

        int pendingCount = (int) requests.stream().filter(
                        r -> r.getStatus() == RequestStatus.PENDING_PEER || r.getStatus() == RequestStatus.PEER_ACCEPTED)
                .count();

        int approvedCount = (int) requests.stream().filter(r -> r.getStatus() == RequestStatus.APPROVED).count();

        List<NurseShiftResponseDto> shiftDtos = upcomingShifts.stream().limit(5).map(nurseServiceImpl::mapToDto)
                .toList();

        List<ShiftRequestResponseDto> requestDtos = requests.stream()
                .sorted(Comparator.comparing(ShiftRequest::getCreatedAt).reversed()).limit(5)
                .map(shiftRequestServiceImpl::mapToDto).toList();

        return NurseDashboardDto.builder().upcomingShiftCount(upcomingShifts.size()).pendingRequestCount(pendingCount)
                .approvedSwapCount(approvedCount).upcomingShifts(shiftDtos).recentRequests(requestDtos).build();
    }

    @Override
    public InchargeDashboardDto getInchargeDashboard(String username) {

        NursingIncharge incharge = nursingInchargeRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Incharge profile not found"));

        Department department = incharge.getDepartment();

        LocalDate today = LocalDate.now();

        // All nurses in department
        List<Nurse> nurses = nurseRepository.findByDepartment(department);

        // Today's assignments
        List<NurseShift> todayAssignments = nurseShiftRepository.findByDepartmentAndDate(department, today);

        int assignedCount = todayAssignments.stream().map(ns -> ns.getNurse().getId())
                .collect(java.util.stream.Collectors.toSet()).size();

        int unassignedCount = nurses.size() - assignedCount;

        // Pending approvals
        int pendingApprovals = shiftRequestRepository.findByDepartmentAndStatus(department, RequestStatus.PEER_ACCEPTED)
                .size();

        // Active swap requests
        int activeRequests = (int) shiftRequestRepository.findByDepartment(department).stream().filter(
                        r -> r.getStatus() == RequestStatus.PENDING_PEER || r.getStatus() == RequestStatus.PEER_ACCEPTED)
                .count();

        List<NurseShiftResponseDto> assignmentDtos = todayAssignments.stream().map(nurseServiceImpl::mapShiftToDto)
                .toList();

        return InchargeDashboardDto.builder().departmentName(department.getName()).totalNurses(nurses.size())
                .assignedToday(assignedCount).unassignedToday(unassignedCount).pendingApprovals(pendingApprovals)
                .activeSwapRequests(activeRequests).todayAssignments(assignmentDtos).build();
    }

    @Override
    public DutyOfficerDashboardDto getDutyOfficerDashboard(String username) {

        DutyOfficer officer = dutyOfficerRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Duty officer not found"));

        Department department = officer.getDepartment();

        LocalDate today = LocalDate.now();

        // Total nurses in department
        List<Nurse> nurses = nurseRepository.findByDepartment(department);

        long totalNurses = nurses.size();

        // Today's assignments
        List<NurseShift> todayAssignments = nurseShiftRepository.findByDepartmentAndDate(department, today);

        long totalAssignedToday = todayAssignments.stream().map(ns -> ns.getNurse().getId()).distinct().count();

        long totalUnassignedToday = totalNurses - totalAssignedToday;

        // All department requests
        List<ShiftRequest> requests = shiftRequestRepository.findByDepartment(department);

        long totalPendingPeerRequests = requests.stream().filter(r -> r.getStatus() == RequestStatus.PENDING_PEER)
                .count();

        long totalPendingInchargeRequests = requests.stream().filter(r -> r.getStatus() == RequestStatus.PEER_ACCEPTED)
                .count();

        long totalApprovedRequests = requests.stream().filter(r -> r.getStatus() == RequestStatus.APPROVED).count();

        return DutyOfficerDashboardDto.builder().departmentName(department.getName()).totalNurses(totalNurses)
                .totalAssignedToday(totalAssignedToday).totalUnassignedToday(totalUnassignedToday)
                .totalPendingPeerRequests(totalPendingPeerRequests)
                .totalPendingInchargeRequests(totalPendingInchargeRequests).totalApprovedRequests(totalApprovedRequests)
                .build();
    }

}
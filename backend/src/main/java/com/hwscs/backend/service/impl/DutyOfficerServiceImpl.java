package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.response.DepartmentStaffingDto;
import com.hwscs.backend.dto.response.NurseResponseDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;
import com.hwscs.backend.dto.response.ShiftRequestResponseDto;
import com.hwscs.backend.dto.response.WeeklyReportDto;
import com.hwscs.backend.dto.response.NurseWeeklyReportDto;
import com.hwscs.backend.dto.response.DailyAssignmentDto;
import com.hwscs.backend.entity.Department;
import com.hwscs.backend.entity.DutyOfficer;
import com.hwscs.backend.entity.Nurse;
import com.hwscs.backend.entity.NurseShift;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.repository.DutyOfficerRepository;
import com.hwscs.backend.repository.NurseRepository;
import com.hwscs.backend.repository.NurseShiftRepository;
import com.hwscs.backend.repository.ShiftRequestRepository;
import com.hwscs.backend.service.interfaces.DutyOfficerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DutyOfficerServiceImpl implements DutyOfficerService {

    private final DutyOfficerRepository dutyOfficerRepository;
    private final NurseRepository nurseRepository;
    private final NurseShiftRepository nurseShiftRepository;
    private final ShiftRequestRepository shiftRequestRepository;
    private final NurseServiceImpl nurseServiceImpl;
    private final ShiftRequestServiceImpl shiftRequestServiceImpl;


    public DutyOfficerServiceImpl(DutyOfficerRepository dutyOfficerRepository, NurseRepository nurseRepository,
                                  NurseShiftRepository nurseShiftRepository, ShiftRequestRepository shiftRequestRepository,
                                  NurseServiceImpl nurseServiceImpl, ShiftRequestServiceImpl shiftRequestServiceImpl) {
        super();
        this.dutyOfficerRepository = dutyOfficerRepository;
        this.nurseRepository = nurseRepository;
        this.nurseShiftRepository = nurseShiftRepository;
        this.shiftRequestRepository = shiftRequestRepository;
        this.nurseServiceImpl = nurseServiceImpl;
        this.shiftRequestServiceImpl = shiftRequestServiceImpl;
    }

    @Override
    public DepartmentStaffingDto getDailyStaffingReport(String dutyOfficerUsername, LocalDate date) {

        DutyOfficer officer = getOfficerByUsername(dutyOfficerUsername);
        Department dept = officer.getDepartment();

        List<Nurse> allNurses = nurseRepository.findByDepartment(dept);
        List<NurseShift> assignments = nurseShiftRepository.findByDepartmentAndDate(dept, date);

        Set<Integer> assignedNurseIds = assignments.stream().map(ns -> ns.getNurse().getId())
                .collect(Collectors.toSet());

        List<NurseShiftResponseDto> assignmentDtos = assignments.stream().map(nurseServiceImpl::mapShiftToDto).toList();

        return DepartmentStaffingDto.builder().departmentName(dept.getName()).date(date).totalNurse(allNurses.size())
                .assignedNurses(assignedNurseIds.size()).unassignedNurses(allNurses.size() - assignedNurseIds.size())
                .assignments(assignmentDtos).build();
    }

    @Override
    public List<NurseResponseDto> getDepartmentNurses(String dutyOfficerUsername) {
        DutyOfficer officer = getOfficerByUsername(dutyOfficerUsername);
        return nurseRepository.findByDepartment(officer.getDepartment()).stream().map(nurseServiceImpl::mapToDto)
                .toList();
    }

    @Override
    public List<ShiftRequestResponseDto> getDepartmentShiftRequests(String dutyOfficerUsername) {
        DutyOfficer officer = getOfficerByUsername(dutyOfficerUsername);
        return shiftRequestRepository.findByDepartment(officer.getDepartment()).stream()
                .map(shiftRequestServiceImpl::mapToDto).toList();
    }

    @Override
    public WeeklyReportDto getWeeklyReport(String dutyOfficerUsername, LocalDate startDate) {
        DutyOfficer officer = getOfficerByUsername(dutyOfficerUsername);
        Department dept = officer.getDepartment();

        // Calculate Monday to Sunday of the targeted week
        LocalDate from = (startDate != null) ? startDate : LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate to = from.plusDays(6);

        List<Nurse> allNurses = nurseRepository.findByDepartment(dept);
        List<NurseShift> allAssignments = nurseShiftRepository.findByDepartmentAndDateRange(dept, from, to);
        List<com.hwscs.backend.entity.ShiftRequest> allRequests = shiftRequestRepository.findByDepartment(dept);

        List<NurseWeeklyReportDto> nurseReports = new java.util.ArrayList<>();

        for (Nurse nurse : allNurses) {
            List<DailyAssignmentDto> dailyAssignments = new java.util.ArrayList<>();
            int totalShifts = 0;

            // Map each of the 7 days of the week
            for (int i = 0; i < 7; i++) {
                LocalDate dayDate = from.plusDays(i);
                String dayOfWeek = dayDate.getDayOfWeek().name();

                // Find if the nurse has an assignment on this day
                final LocalDate currentDay = dayDate;
                NurseShift assignment = allAssignments.stream()
                        .filter(ns -> ns.getNurse().getId().equals(nurse.getId()) && ns.getShiftDate().equals(currentDay))
                        .findFirst()
                        .orElse(null);

                if (assignment != null) {
                    totalShifts++;
                    dailyAssignments.add(DailyAssignmentDto.builder()
                            .date(dayDate)
                            .dayOfWeek(dayOfWeek)
                            .shiftName(assignment.getShift().getShiftName())
                            .shiftTime(assignment.getShift().getStartTime().toString() + " - " + assignment.getShift().getEndTime().toString())
                            .build());
                } else {
                    dailyAssignments.add(DailyAssignmentDto.builder()
                            .date(dayDate)
                            .dayOfWeek(dayOfWeek)
                            .shiftName("OFF")
                            .shiftTime(null)
                            .build());
                }
            }

            // Count approved/pending swap requests for this nurse in this week
            int approvedCount = 0;
            int pendingCount = 0;

            for (com.hwscs.backend.entity.ShiftRequest req : allRequests) {
                // Check if the request involves the current nurse
                boolean involvesNurse = req.getRequesterNurse().getId().equals(nurse.getId()) ||
                        req.getPeerNurse().getId().equals(nurse.getId());
                if (!involvesNurse) {
                    continue;
                }

                // Check if the request dates fall in the target week
                LocalDate reqDate = req.getRequesterNurseShift().getShiftDate();
                boolean isInWeek = (reqDate.isEqual(from) || reqDate.isAfter(from)) && (reqDate.isEqual(to) || reqDate.isBefore(to));
                if (!isInWeek) {
                    continue;
                }

                if (req.getStatus() == com.hwscs.backend.enums.RequestStatus.APPROVED) {
                    approvedCount++;
                } else if (req.getStatus() == com.hwscs.backend.enums.RequestStatus.PENDING_PEER || req.getStatus() == com.hwscs.backend.enums.RequestStatus.PEER_ACCEPTED) {
                    pendingCount++;
                }
            }

            nurseReports.add(NurseWeeklyReportDto.builder()
                    .nurseId(nurse.getId())
                    .fullName(nurse.getFullName())
                    .employeeCode(nurse.getEmployeeCode())
                    .contactEmail(nurse.getContactEmail())
                    .contactPhone(nurse.getContactPhone())
                    .nurseType(nurse.getNurseType().name())
                    .allowShiftChange(nurse.getAllowShiftChange())
                    .totalShifts(totalShifts)
                    .swapsApprovedCount(approvedCount)
                    .swapsPendingCount(pendingCount)
                    .dailyAssignments(dailyAssignments)
                    .build());
        }

        return WeeklyReportDto.builder()
                .departmentName(dept.getName())
                .startDate(from)
                .endDate(to)
                .nurses(nurseReports)
                .build();
    }

    private DutyOfficer getOfficerByUsername(String username) {
        return dutyOfficerRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Duty officer profile not found for: " + username));
    }
}
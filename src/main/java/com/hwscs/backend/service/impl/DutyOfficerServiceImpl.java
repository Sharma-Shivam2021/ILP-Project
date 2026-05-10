package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.response.DepartmentStaffingDto;
import com.hwscs.backend.dto.response.NurseResponseDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;
import com.hwscs.backend.dto.response.ShiftRequestResponseDto;
import com.hwscs.backend.entity.Department;
import com.hwscs.backend.entity.DutyOfficer;
import com.hwscs.backend.entity.Nurse;
import com.hwscs.backend.entity.NurseShift;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.repository.DutyOfficerRepository;
import com.hwscs.backend.repository.NurseRepository;
import com.hwscs.backend.repository.NurseShiftRepository;
import com.hwscs.backend.repository.ShiftRequestRepository;
import com.hwscs.backend.service.DutyOfficerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DutyOfficerServiceImpl implements DutyOfficerService {

    private final DutyOfficerRepository dutyOfficerRepository;
    private final NurseRepository nurseRepository;
    private final NurseShiftRepository nurseShiftRepository;
    private final ShiftRequestRepository shiftRequestRepository;
    private final NurseServiceImpl nurseServiceImpl;
    private final ShiftRequestServiceImpl shiftRequestServiceImpl;

    @Override
    public DepartmentStaffingDto getDailyStaffingReport(String dutyOfficerUsername, LocalDate date) {

        DutyOfficer officer = getOfficerByUsername(dutyOfficerUsername);
        Department dept = officer.getDepartment();

        List<Nurse> allNurses = nurseRepository.findByDepartment(dept);
        List<NurseShift> assignments = nurseShiftRepository.findByDepartmentAndDate(dept, date);

        Set<Integer> assignedNurseIds = assignments.stream()
                .map(ns -> ns.getNurse().getId())
                .collect(Collectors.toSet());

        List<NurseShiftResponseDto> assignmentDtos = assignments.stream()
                .map(nurseServiceImpl::mapShiftToDto)
                .toList();

        return DepartmentStaffingDto.builder()
                .departmentName(dept.getName())
                .date(date)
                .totalNurses(allNurses.size())
                .assignedNurses(assignedNurseIds.size())
                .unassignedNurses(allNurses.size() - assignedNurseIds.size())
                .assignments(assignmentDtos)
                .build();
    }

    @Override
    public List<NurseResponseDto> getDepartmentNurses(String dutyOfficerUsername) {
        DutyOfficer officer = getOfficerByUsername(dutyOfficerUsername);
        return nurseRepository.findByDepartment(officer.getDepartment())
                .stream()
                .map(nurseServiceImpl::mapToDto)
                .toList();
    }

    @Override
    public List<ShiftRequestResponseDto> getDepartmentShiftRequests(String dutyOfficerUsername) {
        DutyOfficer officer = getOfficerByUsername(dutyOfficerUsername);
        return shiftRequestRepository.findByDepartment(officer.getDepartment())
                .stream()
                .map(shiftRequestServiceImpl::mapToDto)
                .toList();
    }

    private DutyOfficer getOfficerByUsername(String username) {
        return dutyOfficerRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Duty officer profile not found for: " + username));
    }
}

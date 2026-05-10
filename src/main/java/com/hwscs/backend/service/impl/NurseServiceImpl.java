package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.response.NurseResponseDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;
import com.hwscs.backend.entity.Department;
import com.hwscs.backend.entity.Nurse;
import com.hwscs.backend.entity.NurseShift;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.repository.DepartmentRepository;
import com.hwscs.backend.repository.NurseRepository;
import com.hwscs.backend.repository.NurseShiftRepository;
import com.hwscs.backend.service.interfaces.NurseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;
    private final NurseShiftRepository nurseShiftRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public NurseResponseDto getMyProfile(String username) {
        Nurse nurse = getNurseByUsername(username);
        return mapToDto(nurse);
    }

    @Override
    public List<NurseShiftResponseDto> getMyShifts(String username) {
        Nurse nurse = getNurseByUsername(username);
        return nurseShiftRepository.findByNurse(nurse)
                .stream()
                .map(this::mapShiftToDto)
                .toList();
    }

    @Override
    public List<NurseResponseDto> getNursesByDepartment(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + departmentId));
        return nurseRepository.findByDepartment(department)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public NurseResponseDto getNurseById(Integer nurseId) {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found: " + nurseId));
        return mapToDto(nurse);
    }

    // --- helpers ---

    private Nurse getNurseByUsername(String username) {
        return nurseRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse profile not found for user: " + username));
    }

    public NurseResponseDto mapToDto(Nurse nurse) {
        return NurseResponseDto.builder()
                .id(nurse.getId())
                .employeeCode(nurse.getEmployeeCode())
                .fullName(nurse.getFullName())
                .nurseType(nurse.getNurseType().name())
                .contactPhone(nurse.getContactPhone())
                .contactEmail(nurse.getContactEmail())
                .departmentName(nurse.getDepartment().getName())
                .username(nurse.getUser().getUsername())
                .build();
    }

    public NurseShiftResponseDto mapShiftToDto(NurseShift ns) {
        return NurseShiftResponseDto.builder()
                .id(ns.getId())
                .nurseId(ns.getNurse().getId())
                .nurseFullName(ns.getNurse().getFullName())
                .shiftDate(ns.getShiftDate())
                .shiftName(ns.getShift().getShiftName())
                .startTime(ns.getShift().getStartTime())
                .endTime(ns.getShift().getEndTime())
                .isSwapped(ns.getIsSwapped())
                .build();
    }
}

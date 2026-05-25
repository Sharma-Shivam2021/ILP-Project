package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.response.NurseResponseDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;
import com.hwscs.backend.entity.*;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.exception.UnauthorizedActionException;
import com.hwscs.backend.repository.*;
import com.hwscs.backend.service.interfaces.NurseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;
    private final NurseShiftRepository nurseShiftRepository;
    private final DepartmentRepository departmentRepository;
    private final NursingInchargeRepository nursingInchargeRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public NurseServiceImpl(NurseRepository nurseRepository, NurseShiftRepository nurseShiftRepository,
                            DepartmentRepository departmentRepository, NursingInchargeRepository nursingInchargeRepository,
                            UserRepository userRepository, AuditLogRepository auditLogRepository) {
        super();
        this.nurseRepository = nurseRepository;
        this.nurseShiftRepository = nurseShiftRepository;
        this.departmentRepository = departmentRepository;
        this.nursingInchargeRepository = nursingInchargeRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

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

    @Override
    @Transactional
    public NurseResponseDto updateAllowShiftChange(Integer nurseId, Boolean allowShiftChange, String actorUsername) {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found with ID: " + nurseId));

        User actor = userRepository.findByUsername(actorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Actor user not found: " + actorUsername));

        // RBAC Check: Nursing Incharge can only update nurses in their department
        if (actor.getRole() == com.hwscs.backend.enums.Role.NURSING_INCHARGE) {
            NursingIncharge incharge = nursingInchargeRepository.findByUser_Username(actorUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("Nursing incharge profile not found"));
            if (!incharge.getDepartment().getId().equals(nurse.getDepartment().getId())) {
                throw new UnauthorizedActionException("You can only manage nurses within your own department");
            }
        } else if (actor.getRole() != com.hwscs.backend.enums.Role.ADMIN) {
            throw new UnauthorizedActionException("Only Nursing In-charge or Admin can change this setting");
        }

        nurse.setAllowShiftChange(allowShiftChange);
        Nurse saved = nurseRepository.save(nurse);

        // Audit Log
        AuditLog audit = AuditLog.builder()
                .action("PERMISSION_CHANGE")
                .performedBy(actorUsername)
                .targetUser(nurse.getUser().getUsername())
                .details("Set Allow Shift Change to " + allowShiftChange + " for nurse " + nurse.getUser().getUsername())
                .build();
        auditLogRepository.save(audit);

        return mapToDto(saved);
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
                .allowShiftChange(nurse.getAllowShiftChange())
                .build();
    }

    public NurseShiftResponseDto mapToDto(NurseShift nurseShift) {
        return NurseShiftResponseDto.builder()
                .id(nurseShift.getId())
                .nurseId(nurseShift.getNurse().getId())
                .nurseFullName(nurseShift.getNurse().getFullName())
                .shiftDate(nurseShift.getShiftDate())
                .shiftName(nurseShift.getShift().getShiftName())
                .startTime(nurseShift.getShift().getStartTime())
                .endTime(nurseShift.getShift().getEndTime())
                .isSwapped(nurseShift.getIsSwapped())
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
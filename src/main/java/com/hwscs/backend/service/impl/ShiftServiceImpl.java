package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.request.AssignShiftDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;
import com.hwscs.backend.entity.Department;
import com.hwscs.backend.entity.Nurse;
import com.hwscs.backend.entity.NurseShift;
import com.hwscs.backend.entity.Shift;
import com.hwscs.backend.exception.InvalidRequestException;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.repository.DepartmentRepository;
import com.hwscs.backend.repository.NurseRepository;
import com.hwscs.backend.repository.NurseShiftRepository;
import com.hwscs.backend.repository.ShiftRepository;
import com.hwscs.backend.service.interfaces.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final NurseRepository nurseRepository;
    private final ShiftRepository shiftRepository;
    private final NurseShiftRepository nurseShiftRepository;
    private final DepartmentRepository departmentRepository;
    private final NurseServiceImpl nurseServiceImpl;

    @Override
    @Transactional
    public NurseShiftResponseDto assignShift(AssignShiftDto dto) {

        Nurse nurse = nurseRepository.findById(dto.getNurseId())
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found: " + dto.getNurseId()));

        Shift shift = shiftRepository.findById(dto.getShiftId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found: " + dto.getShiftId()));

        // Only future shifts can be assigned
        if (!dto.getShiftDate().isAfter(LocalDate.now().minusDays(1))) {
            throw new InvalidRequestException("Shifts can only be assigned for today or future dates");
        }

        // Check if nurse already has a shift on this date
        nurseShiftRepository.findByNurseAndShiftDate(nurse, dto.getShiftDate())
                .ifPresent(existing -> {
                    throw new InvalidRequestException(
                            "Nurse " + nurse.getFullName() + " already has a shift on " + dto.getShiftDate()
                    );
                });

        NurseShift nurseShift = NurseShift.builder()
                .nurse(nurse)
                .shift(shift)
                .shiftDate(dto.getShiftDate())
                .build();

        NurseShift saved = nurseShiftRepository.save(nurseShift);
        return nurseServiceImpl.mapShiftToDto(saved);
    }

    @Override
    public List<NurseShiftResponseDto> getDepartmentSchedule(Integer departmentId, LocalDate date) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + departmentId));

        return nurseShiftRepository.findByDepartmentAndDate(department, date)
                .stream()
                .map(nurseServiceImpl::mapShiftToDto)
                .toList();
    }
}

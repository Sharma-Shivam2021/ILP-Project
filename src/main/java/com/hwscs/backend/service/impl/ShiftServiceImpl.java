package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.requests.AssignShiftDto;
import com.hwscs.backend.dto.requests.CreateShiftDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;
import com.hwscs.backend.dto.response.ShiftResponseDto;
import com.hwscs.backend.entity.Department;
import com.hwscs.backend.entity.Nurse;
import com.hwscs.backend.entity.NurseShift;
import com.hwscs.backend.entity.Shift;
import com.hwscs.backend.exception.InvalidRequestException;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.repository.*;
import com.hwscs.backend.service.interfaces.ShiftService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShiftServiceImpl implements ShiftService {

    private final NurseRepository nurseRepository;
    private final NursingInchargeRepository nursingInchargeRepository;
    private final ShiftRepository shiftRepository;
    private final NurseShiftRepository nurseShiftRepository;
    private final DepartmentRepository departmentRepository;
    private final NurseServiceImpl nurseServiceImpl;

    public ShiftServiceImpl(NurseRepository nurseRepository, ShiftRepository shiftRepository,
                            NurseShiftRepository nurseShiftRepository, DepartmentRepository departmentRepository,
                            NurseServiceImpl nurseServiceImpl, NursingInchargeRepository nursingInchargeRepository) {
        super();
        this.nurseRepository = nurseRepository;
        this.shiftRepository = shiftRepository;
        this.nurseShiftRepository = nurseShiftRepository;
        this.departmentRepository = departmentRepository;
        this.nurseServiceImpl = nurseServiceImpl;
        this.nursingInchargeRepository = nursingInchargeRepository;
    }

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
        nurseShiftRepository.findByNurseAndShiftDate(nurse, dto.getShiftDate()).ifPresent(existing -> {
            throw new InvalidRequestException(
                    "Nurse " + nurse.getFullName() + " already has a shift on " + dto.getShiftDate());
        });

        NurseShift nurseShift = NurseShift.builder().nurse(nurse).shift(shift).shiftDate(dto.getShiftDate()).build();

        NurseShift saved = nurseShiftRepository.save(nurseShift);
        return nurseServiceImpl.mapShiftToDto(saved);
    }

    @Override
    public List<NurseShiftResponseDto> getDepartmentSchedule(Integer departmentId, LocalDate date) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + departmentId));

        return nurseShiftRepository.findByDepartmentAndDate(department, date).stream()
                .map(nurseServiceImpl::mapShiftToDto).toList();
    }

    @Override
    @Transactional
    public ShiftResponseDto createShift(CreateShiftDto dto, String username) {
        nursingInchargeRepository.findByUser_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Nursing incharge profile not found"));
        Shift shift = Shift.builder().shiftName(dto.getShiftName()).startTime(dto.getStartTime())
                .endTime(dto.getEndTime()).active(true).build();

        return mapToDto(shiftRepository.save(shift));
    }

    @Override
    public List<ShiftResponseDto> getAllShifts() {
        return shiftRepository.findAll().stream().map(this::mapToDto).toList();
    }

    private ShiftResponseDto mapToDto(Shift shift) {
        return ShiftResponseDto.builder().id(shift.getId()).shiftName(shift.getShiftName())
                .startTime(shift.getStartTime()).endTime(shift.getEndTime()).active(shift.getActive()).build();
    }

}
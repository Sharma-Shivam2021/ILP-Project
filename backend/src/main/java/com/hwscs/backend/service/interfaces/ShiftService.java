package com.hwscs.backend.service.interfaces;

import com.hwscs.backend.dto.requests.AssignShiftDto;
import com.hwscs.backend.dto.requests.CreateShiftDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;
import com.hwscs.backend.dto.response.ShiftResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ShiftService {
    NurseShiftResponseDto assignShift(AssignShiftDto dto);

    List<NurseShiftResponseDto> getDepartmentSchedule(Integer departmentId, LocalDate date);

    ShiftResponseDto createShift(CreateShiftDto dto, String username);

    List<ShiftResponseDto> getAllShifts();
}
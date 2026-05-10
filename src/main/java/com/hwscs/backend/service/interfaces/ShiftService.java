package com.hwscs.backend.service.interfaces;

import com.hwscs.backend.dto.request.AssignShiftDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ShiftService {

    NurseShiftResponseDto assignShift(AssignShiftDto dto);

    List<NurseShiftResponseDto> getDepartmentSchedule(Integer departmentId, LocalDate date);
}

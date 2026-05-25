package com.hwscs.backend.service.interfaces;

import com.hwscs.backend.dto.response.DepartmentStaffingDto;
import com.hwscs.backend.dto.response.NurseResponseDto;
import com.hwscs.backend.dto.response.ShiftRequestResponseDto;
import com.hwscs.backend.dto.response.WeeklyReportDto;

import java.time.LocalDate;
import java.util.List;

public interface DutyOfficerService {

    DepartmentStaffingDto getDailyStaffingReport(String dutyOfficerUsername, LocalDate date);

    List<NurseResponseDto> getDepartmentNurses(String dutyOfficerUsername);

    List<ShiftRequestResponseDto> getDepartmentShiftRequests(String dutyOfficerUsername);

    WeeklyReportDto getWeeklyReport(String dutyOfficerUsername, LocalDate startDate);
}

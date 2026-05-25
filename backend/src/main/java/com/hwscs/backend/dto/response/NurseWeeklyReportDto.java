package com.hwscs.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NurseWeeklyReportDto {
    private Integer nurseId;
    private String fullName;
    private String employeeCode;
    private String contactEmail;
    private String contactPhone;
    private String nurseType;
    private Boolean allowShiftChange;
    private Integer totalShifts;
    private Integer swapsApprovedCount;
    private Integer swapsPendingCount;
    private List<DailyAssignmentDto> dailyAssignments;
}

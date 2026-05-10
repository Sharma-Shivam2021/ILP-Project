package com.hwscs.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentStaffingDto {

    private String departmentName;
    private LocalDate date;
    private int totalNurses;
    private int assignedNurses;
    private int unassignedNurses;
    private List<NurseShiftResponseDto> assignments;
}

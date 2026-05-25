package com.hwscs.backend.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class EligiblePeerDto {
    private Integer nurseId;
    private String nurseName;
    private Integer nurseShiftId;
    private String shiftName;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;
    private LocalDate shiftDate;
    
    // Additional fields for enhanced searching
    private String employeeCode;
    private String contactEmail;
    private String contactPhone;
    private String departmentName;
    private String nurseType;
}
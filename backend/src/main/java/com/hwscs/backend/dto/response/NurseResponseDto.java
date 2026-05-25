package com.hwscs.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class NurseResponseDto {
    private Integer id;
    private String employeeCode;
    private String fullName;
    private String nurseType;
    private String contactPhone;
    private String contactEmail;
    private String departmentName;
    private String username;
    private Boolean allowShiftChange;
}
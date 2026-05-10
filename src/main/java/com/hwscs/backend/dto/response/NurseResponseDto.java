package com.hwscs.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NurseResponseDto {

    private Integer id;
    private String employeeCode;
    private String fullName;
    private String nurseType;
    private String contactPhone;
    private String contactEmail;
    private String departmentName;
    private String username;
}

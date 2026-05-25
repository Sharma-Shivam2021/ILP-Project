package com.hwscs.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AdminUserResponseDto {
    private Long userId;
    private String username;
    private String role;
    private Integer profileId;
    private String employeeCode;
    private String fullName;
    private String email;
    private String contactPhone;
    private String departmentName;
    private Integer departmentId;
    private Boolean isActive;
    private String nurseType;
}

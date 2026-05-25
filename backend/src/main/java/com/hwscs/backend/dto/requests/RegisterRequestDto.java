package com.hwscs.backend.dto.requests;

import com.hwscs.backend.enums.NurseType;
import com.hwscs.backend.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {

    @NotBlank(message = "Username is required")
    private String username;

    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Please enter a valid 10-digit Indian mobile number starting with 6-9."
    )
    private String contactPhone;

    @NotNull(message = "Role is required")
    private Role role;

    @NotBlank(message = "Employee code is required")
    private String employeeCode;

    // Optional for Admin role
    private Integer departmentId;

    // Only needed for Nurse role
    private NurseType nurseType;
}

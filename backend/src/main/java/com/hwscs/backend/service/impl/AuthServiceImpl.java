package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.requests.LoginRequestDto;
import com.hwscs.backend.dto.requests.RegisterRequestDto;
import com.hwscs.backend.dto.response.LoginResponseDto;
import com.hwscs.backend.entity.*;
import com.hwscs.backend.exception.InvalidRequestException;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.repository.*;
import com.hwscs.backend.service.interfaces.AuthService;
import com.hwscs.backend.service.interfaces.LoginAttemptService;
import com.hwscs.backend.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;
    private final JwtUtil jwtUtil;
    
    private final DepartmentRepository departmentRepository;
    private final NurseRepository nurseRepository;
    private final NursingInchargeRepository nursingInchargeRepository;
    private final DutyOfficerRepository dutyOfficerRepository;
    private final AdminRepository adminRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                           UserRepository userRepository, JwtUtil jwtUtil, LoginAttemptService loginAttemptService,
                           DepartmentRepository departmentRepository, NurseRepository nurseRepository,
                           NursingInchargeRepository nursingInchargeRepository, DutyOfficerRepository dutyOfficerRepository,
                           AdminRepository adminRepository, AuditLogRepository auditLogRepository,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.loginAttemptService = loginAttemptService;
        this.departmentRepository = departmentRepository;
        this.nurseRepository = nurseRepository;
        this.nursingInchargeRepository = nursingInchargeRepository;
        this.dutyOfficerRepository = dutyOfficerRepository;
        this.adminRepository = adminRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidRequestException("Invalid Username or Password"));

        if (user.getAccountLocked()) {
            boolean unlocked = loginAttemptService.unlockWhenTimeExpired(user);
            if (!unlocked) {
                throw new InvalidRequestException("Account temporarily locked. Try again later");
            }
        }

        try {
            // Throws BadCredentialsException if invalid — caught by GlobalExceptionHandler
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            loginAttemptService.loginSucceeded(user);
        } catch (Exception e) {
            loginAttemptService.loginFailed(user);
            throw new InvalidRequestException("Invalid Username or Password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        String fullName = user.getUsername();
        switch (user.getRole()) {
            case NURSE -> {
                fullName = nurseRepository.findByUser_Id(user.getId().intValue())
                        .map(Nurse::getFullName).orElse(user.getUsername());
            }
            case NURSING_INCHARGE -> {
                fullName = nursingInchargeRepository.findByUser_Id(user.getId().intValue())
                        .map(NursingIncharge::getFullName).orElse(user.getUsername());
            }
            case DUTY_OFFICER -> {
                fullName = dutyOfficerRepository.findByUser_Id(user.getId().intValue())
                        .map(DutyOfficer::getFullName).orElse(user.getUsername());
            }
            case ADMIN -> {
                fullName = adminRepository.findByUser_Username(user.getUsername())
                        .map(Admin::getFullName).orElse(user.getUsername());
            }
        }

        return LoginResponseDto.builder().token(token).username(user.getUsername()).fullName(fullName).firstLogin(user.getFirstLogin()).role(user.getRole().name())
                .userId(user.getId()).build();
    }

    @Override
    @Transactional
    public void register(RegisterRequestDto dto) {
        // Validate password
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new InvalidRequestException("Password is required");
        }
        if (!dto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$")) {
            throw new InvalidRequestException("Password must be at least 8 characters and contain uppercase, lowercase, number, and special characters.");
        }

        // 1. Duplicate checks
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new InvalidRequestException("Username is already taken");
        }

        // Email & phone check across all tables
        String email = dto.getEmail();
        String phone = dto.getContactPhone();
        String code = dto.getEmployeeCode();

        if (nurseRepository.findByContactEmail(email).isPresent() ||
            nursingInchargeRepository.findByContactEmail(email).isPresent() ||
            dutyOfficerRepository.findByContactEmail(email).isPresent() ||
            adminRepository.findByContactEmail(email).isPresent()) {
            throw new InvalidRequestException("Email is already registered");
        }

        if (nurseRepository.findByContactPhone(phone).isPresent() ||
            nursingInchargeRepository.findByContactPhone(phone).isPresent() ||
            dutyOfficerRepository.findByContactPhone(phone).isPresent() ||
            adminRepository.findByContactPhone(phone).isPresent()) {
            throw new InvalidRequestException("Mobile number is already registered");
        }

        if (nurseRepository.findByEmployeeCode(code).isPresent() ||
            nursingInchargeRepository.findByEmployeeCode(code).isPresent() ||
            dutyOfficerRepository.findByEmployeeCode(code).isPresent() ||
            adminRepository.findByEmployeeCode(code).isPresent()) {
            throw new InvalidRequestException("Employee code is already registered");
        }

        // Validate department for non-Admin users
        Department dept = null;
        if (dto.getRole() != com.hwscs.backend.enums.Role.ADMIN) {
            if (dto.getDepartmentId() == null) {
                throw new InvalidRequestException("Department is required for this role");
            }
            dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + dto.getDepartmentId()));
        }

        // 2. Create and save User
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .department(dept)
                .isActive(true)
                .firstLogin(false) // Registered user sets password during sign-up
                .build();
        user = userRepository.save(user);

        // 3. Create role-specific details
        switch (dto.getRole()) {
            case NURSE -> {
                if (dto.getNurseType() == null) {
                    throw new InvalidRequestException("Nurse type is required");
                }
                Nurse nurse = Nurse.builder()
                        .user(user)
                        .department(dept)
                        .employeeCode(code)
                        .fullName(dto.getFullName())
                        .nurseType(dto.getNurseType())
                        .contactPhone(phone)
                        .contactEmail(email)
                        .allowShiftChange(true) // Default to allowed
                        .build();
                nurseRepository.save(nurse);
            }
            case NURSING_INCHARGE -> {
                NursingIncharge incharge = NursingIncharge.builder()
                        .user(user)
                        .department(dept)
                        .employeeCode(code)
                        .fullName(dto.getFullName())
                        .contactPhone(phone)
                        .contactEmail(email)
                        .build();
                nursingInchargeRepository.save(incharge);
            }
            case DUTY_OFFICER -> {
                DutyOfficer officer = DutyOfficer.builder()
                        .user(user)
                        .department(dept)
                        .employeeCode(code)
                        .fullName(dto.getFullName())
                        .contactPhone(phone)
                        .contactEmail(email)
                        .build();
                dutyOfficerRepository.save(officer);
            }
            case ADMIN -> {
                Admin admin = Admin.builder()
                        .user(user)
                        .employeeCode(code)
                        .fullName(dto.getFullName())
                        .contactPhone(phone)
                        .contactEmail(email)
                        .build();
                adminRepository.save(admin);
            }
        }

        // 4. Save Audit Log
        AuditLog audit = AuditLog.builder()
                .action("USER_CREATION")
                .performedBy("SYSTEM_REGISTRATION")
                .targetUser(dto.getUsername())
                .details("Registered user " + dto.getUsername() + " with role " + dto.getRole())
                .build();
        auditLogRepository.save(audit);
    }
}
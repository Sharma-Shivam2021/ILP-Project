package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.requests.RegisterRequestDto;
import com.hwscs.backend.dto.response.AdminUserResponseDto;
import com.hwscs.backend.entity.*;
import com.hwscs.backend.enums.Role;
import com.hwscs.backend.exception.InvalidRequestException;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.repository.*;
import com.hwscs.backend.service.interfaces.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final NurseRepository nurseRepository;
    private final NursingInchargeRepository nursingInchargeRepository;
    private final DutyOfficerRepository dutyOfficerRepository;
    private final AdminRepository adminRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(UserRepository userRepository, NurseRepository nurseRepository,
                            NursingInchargeRepository nursingInchargeRepository, DutyOfficerRepository dutyOfficerRepository,
                            AdminRepository adminRepository, DepartmentRepository departmentRepository,
                            AuditLogRepository auditLogRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.nurseRepository = nurseRepository;
        this.nursingInchargeRepository = nursingInchargeRepository;
        this.dutyOfficerRepository = dutyOfficerRepository;
        this.adminRepository = adminRepository;
        this.departmentRepository = departmentRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<AdminUserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<AdminUserResponseDto> dtos = new ArrayList<>();

        for (User user : users) {
            AdminUserResponseDto dto = AdminUserResponseDto.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .role(user.getRole().name())
                    .isActive(user.getIsActive())
                    .build();

            if (user.getRole() == Role.NURSE) {
                nurseRepository.findByUser_Id(user.getId().intValue()).ifPresent(n -> {
                    dto.setProfileId(n.getId());
                    dto.setEmployeeCode(n.getEmployeeCode());
                    dto.setFullName(n.getFullName());
                    dto.setEmail(n.getContactEmail());
                    dto.setContactPhone(n.getContactPhone());
                    dto.setDepartmentName(n.getDepartment().getName());
                    dto.setDepartmentId(n.getDepartment().getId());
                    dto.setNurseType(n.getNurseType().name());
                });
            } else if (user.getRole() == Role.NURSING_INCHARGE) {
                nursingInchargeRepository.findByUser_Id(user.getId().intValue()).ifPresent(ni -> {
                    dto.setProfileId(ni.getId());
                    dto.setEmployeeCode(ni.getEmployeeCode());
                    dto.setFullName(ni.getFullName());
                    dto.setEmail(ni.getContactEmail());
                    dto.setContactPhone(ni.getContactPhone());
                    dto.setDepartmentName(ni.getDepartment().getName());
                    dto.setDepartmentId(ni.getDepartment().getId());
                });
            } else if (user.getRole() == Role.DUTY_OFFICER) {
                dutyOfficerRepository.findByUser_Id(user.getId().intValue()).ifPresent(doUser -> {
                    dto.setProfileId(doUser.getId());
                    dto.setEmployeeCode(doUser.getEmployeeCode());
                    dto.setFullName(doUser.getFullName());
                    dto.setEmail(doUser.getContactEmail());
                    dto.setContactPhone(doUser.getContactPhone());
                    dto.setDepartmentName(doUser.getDepartment().getName());
                    dto.setDepartmentId(doUser.getDepartment().getId());
                });
            } else if (user.getRole() == Role.ADMIN) {
                adminRepository.findByUser_Username(user.getUsername()).ifPresent(ad -> {
                    dto.setProfileId(ad.getId());
                    dto.setEmployeeCode(ad.getEmployeeCode());
                    dto.setFullName(ad.getFullName());
                    dto.setEmail(ad.getContactEmail());
                    dto.setContactPhone(ad.getContactPhone());
                });
            }

            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    @Transactional
    public void updateUser(Long userId, RegisterRequestDto dto, String adminUsername) {
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // 1. Verify duplicates (excluding self)
        String newUsername = dto.getUsername();
        if (!user.getUsername().equals(newUsername) && userRepository.findByUsername(newUsername).isPresent()) {
            throw new InvalidRequestException("Username is already taken");
        }

        // Email / Phone checks excluding self
        String email = dto.getEmail();
        String phone = dto.getContactPhone();
        String code = dto.getEmployeeCode();

        validateExcludingUser(user.getId(), email, phone, code);

        Department dept = null;
        if (dto.getRole() != Role.ADMIN) {
            if (dto.getDepartmentId() == null) {
                throw new InvalidRequestException("Department is required");
            }
            dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        }

        // Keep track if role has changed
        Role oldRole = user.getRole();
        Role newRole = dto.getRole();

        // Update User credentials
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (!dto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$")) {
                throw new InvalidRequestException("Password must be at least 8 characters and contain uppercase, lowercase, number, and special characters.");
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setRole(newRole);
        user.setDepartment(dept);
        userRepository.save(user);

        // Delete old profile details if role changed
        if (oldRole != newRole) {
            deleteProfileDetails(user.getId().intValue(), oldRole);
            createProfileDetails(user, dto, dept);
        } else {
            // Update existing profile details
            updateProfileDetails(user, dto, dept);
        }

        // Audit Log
        AuditLog audit = AuditLog.builder()
                .action("USER_UPDATE")
                .performedBy(adminUsername)
                .targetUser(user.getUsername())
                .details("Updated user details. Role: " + newRole)
                .build();
        auditLogRepository.save(audit);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId, String adminUsername) {
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // Prevent self deletion
        if (user.getUsername().equals(adminUsername)) {
            throw new InvalidRequestException("Admin cannot delete themselves");
        }

        deleteProfileDetails(user.getId().intValue(), user.getRole());
        userRepository.delete(user);

        // Audit Log
        AuditLog audit = AuditLog.builder()
                .action("USER_DELETION")
                .performedBy(adminUsername)
                .targetUser(user.getUsername())
                .details("Deleted user: " + user.getUsername())
                .build();
        auditLogRepository.save(audit);
    }

    @Override
    @Transactional
    public void resetPassword(Long userId, String newPassword, String adminUsername) {
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Audit Log
        AuditLog audit = AuditLog.builder()
                .action("PASSWORD_RESET")
                .performedBy(adminUsername)
                .targetUser(user.getUsername())
                .details("Reset user password by admin")
                .build();
        auditLogRepository.save(audit);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional
    public Department createDepartment(Department department, String adminUsername) {
        if (departmentRepository.count() > 0 && departmentRepository.findAll().stream().anyMatch(d -> d.getName().equalsIgnoreCase(department.getName()))) {
            throw new InvalidRequestException("Department with this name already exists");
        }
        Department saved = departmentRepository.save(department);

        AuditLog audit = AuditLog.builder()
                .action("DEPARTMENT_CREATION")
                .performedBy(adminUsername)
                .details("Created department: " + saved.getName())
                .build();
        auditLogRepository.save(audit);

        return saved;
    }

    @Override
    @Transactional
    public Department updateDepartment(Integer id, Department department, String adminUsername) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));

        if (departmentRepository.findAll().stream().anyMatch(d -> d.getName().equalsIgnoreCase(department.getName()) && !d.getId().equals(id))) {
            throw new InvalidRequestException("Department with this name already exists");
        }

        existing.setName(department.getName());
        existing.setLocation(department.getLocation());
        Department saved = departmentRepository.save(existing);

        AuditLog audit = AuditLog.builder()
                .action("DEPARTMENT_UPDATE")
                .performedBy(adminUsername)
                .details("Updated department ID " + id + " to name " + saved.getName())
                .build();
        auditLogRepository.save(audit);

        return saved;
    }

    @Override
    @Transactional
    public void deleteDepartment(Integer id, String adminUsername) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));

        // Check if users are assigned to department
        if (userRepository.findAll().stream().anyMatch(u -> u.getDepartment() != null && u.getDepartment().getId().equals(id))) {
            throw new InvalidRequestException("Cannot delete department because users are assigned to it");
        }

        departmentRepository.delete(existing);

        AuditLog audit = AuditLog.builder()
                .action("DEPARTMENT_DELETION")
                .performedBy(adminUsername)
                .details("Deleted department: " + existing.getName())
                .build();
        auditLogRepository.save(audit);
    }

    @Override
    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalNurses", nurseRepository.count());
        stats.put("totalIncharges", nursingInchargeRepository.count());
        stats.put("totalDutyOfficers", dutyOfficerRepository.count());
        stats.put("totalAdmins", adminRepository.count());
        stats.put("totalDepartments", departmentRepository.count());
        stats.put("totalAuditLogs", auditLogRepository.count());
        return stats;
    }

    // --- Helpers ---

    private void validateExcludingUser(Long userId, String email, String phone, String code) {
        // Validation check excluding current user
        boolean emailExists = nurseRepository.findByContactEmail(email).map(n -> !n.getUser().getId().equals(userId)).orElse(false) ||
                nursingInchargeRepository.findByContactEmail(email).map(ni -> !ni.getUser().getId().equals(userId)).orElse(false) ||
                dutyOfficerRepository.findByContactEmail(email).map(doUser -> !doUser.getUser().getId().equals(userId)).orElse(false) ||
                adminRepository.findByContactEmail(email).map(ad -> !ad.getUser().getId().equals(userId)).orElse(false);

        if (emailExists) {
            throw new InvalidRequestException("Email is already registered");
        }

        boolean phoneExists = nurseRepository.findByContactPhone(phone).map(n -> !n.getUser().getId().equals(userId)).orElse(false) ||
                nursingInchargeRepository.findByContactPhone(phone).map(ni -> !ni.getUser().getId().equals(userId)).orElse(false) ||
                dutyOfficerRepository.findByContactPhone(phone).map(doUser -> !doUser.getUser().getId().equals(userId)).orElse(false) ||
                adminRepository.findByContactPhone(phone).map(ad -> !ad.getUser().getId().equals(userId)).orElse(false);

        if (phoneExists) {
            throw new InvalidRequestException("Mobile number is already registered");
        }

        boolean codeExists = nurseRepository.findByEmployeeCode(code).map(n -> !n.getUser().getId().equals(userId)).orElse(false) ||
                nursingInchargeRepository.findByEmployeeCode(code).map(ni -> !ni.getUser().getId().equals(userId)).orElse(false) ||
                dutyOfficerRepository.findByEmployeeCode(code).map(doUser -> !doUser.getUser().getId().equals(userId)).orElse(false) ||
                adminRepository.findByEmployeeCode(code).map(ad -> !ad.getUser().getId().equals(userId)).orElse(false);

        if (codeExists) {
            throw new InvalidRequestException("Employee code is already registered");
        }
    }

    private void deleteProfileDetails(Integer userId, Role role) {
        switch (role) {
            case NURSE -> nurseRepository.findByUser_Id(userId).ifPresent(nurseRepository::delete);
            case NURSING_INCHARGE -> nursingInchargeRepository.findByUser_Id(userId).ifPresent(nursingInchargeRepository::delete);
            case DUTY_OFFICER -> dutyOfficerRepository.findByUser_Id(userId).ifPresent(dutyOfficerRepository::delete);
            case ADMIN -> adminRepository.findByUser_Username(userRepository.findById(userId).map(User::getUsername).orElse("")).ifPresent(adminRepository::delete);
        }
    }

    private void createProfileDetails(User user, RegisterRequestDto dto, Department dept) {
        switch (dto.getRole()) {
            case NURSE -> {
                Nurse nurse = Nurse.builder()
                        .user(user)
                        .department(dept)
                        .employeeCode(dto.getEmployeeCode())
                        .fullName(dto.getFullName())
                        .nurseType(dto.getNurseType() != null ? dto.getNurseType() : com.hwscs.backend.enums.NurseType.PERMANENT)
                        .contactPhone(dto.getContactPhone())
                        .contactEmail(dto.getEmail())
                        .allowShiftChange(true)
                        .build();
                nurseRepository.save(nurse);
            }
            case NURSING_INCHARGE -> {
                NursingIncharge incharge = NursingIncharge.builder()
                        .user(user)
                        .department(dept)
                        .employeeCode(dto.getEmployeeCode())
                        .fullName(dto.getFullName())
                        .contactPhone(dto.getContactPhone())
                        .contactEmail(dto.getEmail())
                        .build();
                nursingInchargeRepository.save(incharge);
            }
            case DUTY_OFFICER -> {
                DutyOfficer officer = DutyOfficer.builder()
                        .user(user)
                        .department(dept)
                        .employeeCode(dto.getEmployeeCode())
                        .fullName(dto.getFullName())
                        .contactPhone(dto.getContactPhone())
                        .contactEmail(dto.getEmail())
                        .build();
                dutyOfficerRepository.save(officer);
            }
            case ADMIN -> {
                Admin admin = Admin.builder()
                        .user(user)
                        .employeeCode(dto.getEmployeeCode())
                        .fullName(dto.getFullName())
                        .contactPhone(dto.getContactPhone())
                        .contactEmail(dto.getEmail())
                        .build();
                adminRepository.save(admin);
            }
        }
    }

    private void updateProfileDetails(User user, RegisterRequestDto dto, Department dept) {
        switch (user.getRole()) {
            case NURSE -> nurseRepository.findByUser_Id(user.getId().intValue()).ifPresent(n -> {
                n.setFullName(dto.getFullName());
                n.setContactEmail(dto.getEmail());
                n.setContactPhone(dto.getContactPhone());
                n.setEmployeeCode(dto.getEmployeeCode());
                n.setDepartment(dept);
                if (dto.getNurseType() != null) {
                    n.setNurseType(dto.getNurseType());
                }
                nurseRepository.save(n);
            });
            case NURSING_INCHARGE -> nursingInchargeRepository.findByUser_Id(user.getId().intValue()).ifPresent(ni -> {
                ni.setFullName(dto.getFullName());
                ni.setContactEmail(dto.getEmail());
                ni.setContactPhone(dto.getContactPhone());
                ni.setEmployeeCode(dto.getEmployeeCode());
                ni.setDepartment(dept);
                nursingInchargeRepository.save(ni);
            });
            case DUTY_OFFICER -> dutyOfficerRepository.findByUser_Id(user.getId().intValue()).ifPresent(doUser -> {
                doUser.setFullName(dto.getFullName());
                doUser.setContactEmail(dto.getEmail());
                doUser.setContactPhone(dto.getContactPhone());
                doUser.setEmployeeCode(dto.getEmployeeCode());
                doUser.setDepartment(dept);
                dutyOfficerRepository.save(doUser);
            });
            case ADMIN -> adminRepository.findByUser_Username(user.getUsername()).ifPresent(ad -> {
                ad.setFullName(dto.getFullName());
                ad.setContactEmail(dto.getEmail());
                ad.setContactPhone(dto.getContactPhone());
                ad.setEmployeeCode(dto.getEmployeeCode());
                adminRepository.save(ad);
            });
        }
    }
}

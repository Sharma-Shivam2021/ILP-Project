package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.requests.ChangePasswordDto;
import com.hwscs.backend.dto.requests.UpdateProfileDto;
import com.hwscs.backend.entity.*;
import com.hwscs.backend.exception.InvalidRequestException;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.repository.*;
import com.hwscs.backend.service.interfaces.ProfileService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final NurseRepository nurseRepository;
    private final NursingInchargeRepository nursingInchargeRepository;
    private final DutyOfficerRepository dutyOfficerRepository;
    private final AdminRepository adminRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileServiceImpl(UserRepository userRepository, NurseRepository nurseRepository,
                              NursingInchargeRepository nursingInchargeRepository, DutyOfficerRepository dutyOfficerRepository,
                              AdminRepository adminRepository, AuditLogRepository auditLogRepository,
                              PasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.nurseRepository = nurseRepository;
        this.nursingInchargeRepository = nursingInchargeRepository;
        this.dutyOfficerRepository = dutyOfficerRepository;
        this.adminRepository = adminRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Object getMyProfile(String username) {
        User user = getUser(username);
        return switch (user.getRole()) {
            case NURSE -> nurseRepository.findByUser_Username(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Nurse profile not found"));
            case DUTY_OFFICER -> dutyOfficerRepository.findByUser_Username(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Duty Officer profile not found"));
            case NURSING_INCHARGE -> nursingInchargeRepository.findByUser_Username(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Incharge profile not found"));
            case ADMIN -> adminRepository.findByUser_Username(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Admin profile not found"));
        };
    }

    @Override
    @Transactional
    public Object updateMyProfile(String username, UpdateProfileDto dto) {
        User user = getUser(username);

        switch (user.getRole()) {
            case DUTY_OFFICER -> {
                DutyOfficer officer = dutyOfficerRepository.findByUser_Username(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Duty officer profile was not found"));
                officer.setFullName(dto.getFullName());
                officer.setContactPhone(dto.getContactPhone());
                officer.setContactEmail(dto.getContactEmail());

                return dutyOfficerRepository.save(officer);
            }
            case NURSE -> {
                Nurse nurse = nurseRepository.findByUser_Username(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Nurse profile was not found"));
                nurse.setFullName(dto.getFullName());
                nurse.setContactPhone(dto.getContactPhone());
                nurse.setContactEmail(dto.getContactEmail());

                return nurseRepository.save(nurse);
            }
            case NURSING_INCHARGE -> {
                NursingIncharge nursingIncharge = nursingInchargeRepository.findByUser_Username(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Incharge profile was not found"));
                nursingIncharge.setFullName(dto.getFullName());
                nursingIncharge.setContactPhone(dto.getContactPhone());
                nursingIncharge.setContactEmail(dto.getContactEmail());

                return nursingInchargeRepository.save(nursingIncharge);
            }
            case ADMIN -> {
                Admin admin = adminRepository.findByUser_Username(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Admin profile was not found"));
                admin.setFullName(dto.getFullName());
                admin.setContactPhone(dto.getContactPhone());
                admin.setContactEmail(dto.getContactEmail());

                return adminRepository.save(admin);
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + user.getRole());
        }
    }

    @Override
    @Transactional
    public void changePassword(String username, ChangePasswordDto dto) {
        User user = getUser(username);
        boolean matches = passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword());

        if (!matches) {
            throw new InvalidRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setFirstLogin(false);
        userRepository.save(user);

        // Audit Log
        AuditLog audit = AuditLog.builder()
                .action("PASSWORD_CHANGE")
                .performedBy(username)
                .targetUser(username)
                .details("Changed password successfully")
                .build();
        auditLogRepository.save(audit);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
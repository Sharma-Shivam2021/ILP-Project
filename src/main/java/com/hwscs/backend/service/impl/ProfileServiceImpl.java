package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.requests.ChangePasswordDto;
import com.hwscs.backend.dto.requests.UpdateProfileDto;
import com.hwscs.backend.entity.DutyOfficer;
import com.hwscs.backend.entity.Nurse;
import com.hwscs.backend.entity.NursingIncharge;
import com.hwscs.backend.entity.User;
import com.hwscs.backend.exception.InvalidRequestException;
import com.hwscs.backend.exception.ResourceNotFoundException;
import com.hwscs.backend.repository.DutyOfficerRepository;
import com.hwscs.backend.repository.NurseRepository;
import com.hwscs.backend.repository.NursingInchargeRepository;
import com.hwscs.backend.repository.UserRepository;
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
    private final PasswordEncoder passwordEncoder;

    public ProfileServiceImpl(UserRepository userRepository, NurseRepository nurseRepository,
                              NursingInchargeRepository nursingInchargeRepository, DutyOfficerRepository dutyOfficerRepository,
                              PasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.nurseRepository = nurseRepository;
        this.nursingInchargeRepository = nursingInchargeRepository;
        this.dutyOfficerRepository = dutyOfficerRepository;
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
            default -> throw new IllegalArgumentException("Unexpected value: " + user.getRole());
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
                        .orElseThrow(() -> new ResourceNotFoundException("Duty officer profile was not found"));
                nurse.setFullName(dto.getFullName());
                nurse.setContactPhone(dto.getContactPhone());
                nurse.setContactEmail(dto.getContactEmail());

                return nurseRepository.save(nurse);
            }

            case NURSING_INCHARGE -> {
                NursingIncharge nursingIncharge = nursingInchargeRepository.findByUser_Username(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Duty officer profile was not found"));
                nursingIncharge.setFullName(dto.getFullName());
                nursingIncharge.setContactPhone(dto.getContactPhone());
                nursingIncharge.setContactEmail(dto.getContactEmail());

                return nursingInchargeRepository.save(nursingIncharge);
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
        userRepository.save(user);

    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
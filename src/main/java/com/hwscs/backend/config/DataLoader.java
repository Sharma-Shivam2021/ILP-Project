package com.hwscs.backend.config;

import com.hwscs.backend.entity.*;
import com.hwscs.backend.entity.enums.NurseType;
import com.hwscs.backend.entity.enums.Role;
import com.hwscs.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final NurseRepository nurseRepository;
    private final ShiftRepository shiftRepository;
    private final NurseShiftRepository nurseShiftRepository;
    private final NursingInchargeRepository nursingInchargeRepository;
    private final DutyOfficerRepository dutyOfficerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (departmentRepository.count() > 0) {
            return;
        }

        // ── Department ──────────────────────────────────────────────────────────
        Department icu = Department.builder()
                .name("ICU")
                .location("Block A, Floor 2")
                .build();
        departmentRepository.save(icu);

        // ── Shifts (no type enum — name and times are free-form) ────────────────
        Shift morningShift = Shift.builder()
                .shiftName("Morning")
                .startTime(LocalTime.of(6, 0))
                .endTime(LocalTime.of(14, 0))
                .build();

        Shift eveningShift = Shift.builder()
                .shiftName("Evening")
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(22, 0))
                .build();

        Shift nightShift = Shift.builder()
                .shiftName("Night")
                .startTime(LocalTime.of(22, 0))
                .endTime(LocalTime.of(6, 0))
                .build();

        shiftRepository.save(morningShift);
        shiftRepository.save(eveningShift);
        shiftRepository.save(nightShift);

        // ── Nurse Users ─────────────────────────────────────────────────────────
        User nurseUser1 = User.builder()
                .username("nurse1")
                .password(passwordEncoder.encode("password123"))
                .role(Role.NURSE)
                .department(icu)
                .build();

        User nurseUser2 = User.builder()
                .username("nurse2")
                .password(passwordEncoder.encode("password123"))
                .role(Role.NURSE)
                .department(icu)
                .build();

        userRepository.save(nurseUser1);
        userRepository.save(nurseUser2);

        // ── Nurse Profiles ──────────────────────────────────────────────────────
        Nurse nurse1 = Nurse.builder()
                .user(nurseUser1)
                .department(icu)
                .employeeCode("N001")
                .fullName("Anita Sharma")
                .nurseType(NurseType.PERMANENT)
                .contactPhone("9876543210")
                .contactEmail("anita.sharma@hospital.com")
                .build();

        Nurse nurse2 = Nurse.builder()
                .user(nurseUser2)
                .department(icu)
                .employeeCode("N002")
                .fullName("Priya Singh")
                .nurseType(NurseType.CONTRACTUAL)
                .contactPhone("9876543211")
                .contactEmail("priya.singh@hospital.com")
                .build();

        nurseRepository.save(nurse1);
        nurseRepository.save(nurse2);

        // ── Nurse Shift Assignments ─────────────────────────────────────────────
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate dayAfter = LocalDate.now().plusDays(2);

        NurseShift ns1 = NurseShift.builder()
                .nurse(nurse1)
                .shift(morningShift)
                .shiftDate(tomorrow)
                .build();

        NurseShift ns2 = NurseShift.builder()
                .nurse(nurse2)
                .shift(nightShift)
                .shiftDate(tomorrow)
                .build();

        NurseShift ns3 = NurseShift.builder()
                .nurse(nurse1)
                .shift(eveningShift)
                .shiftDate(dayAfter)
                .build();

        NurseShift ns4 = NurseShift.builder()
                .nurse(nurse2)
                .shift(morningShift)
                .shiftDate(dayAfter)
                .build();

        nurseShiftRepository.save(ns1);
        nurseShiftRepository.save(ns2);
        nurseShiftRepository.save(ns3);
        nurseShiftRepository.save(ns4);

        // ── Nursing Incharge ────────────────────────────────────────────────────
        User inchargeUser = User.builder()
                .username("incharge1")
                .password(passwordEncoder.encode("password123"))
                .role(Role.NURSING_INCHARGE)
                .department(icu)
                .build();
        userRepository.save(inchargeUser);

        NursingIncharge incharge = NursingIncharge.builder()
                .user(inchargeUser)
                .department(icu)
                .employeeCode("NI001")
                .fullName("Dr. Rekha Verma")
                .contactPhone("9876543212")
                .contactEmail("rekha.verma@hospital.com")
                .build();
        nursingInchargeRepository.save(incharge);

        // ── Duty Officer ────────────────────────────────────────────────────────
        User officerUser = User.builder()
                .username("officer1")
                .password(passwordEncoder.encode("password123"))
                .role(Role.DUTY_OFFICER)
                .department(icu)
                .build();
        userRepository.save(officerUser);

        DutyOfficer officer = DutyOfficer.builder()
                .user(officerUser)
                .department(icu)
                .employeeCode("DO001")
                .fullName("Mr. Suresh Patil")
                .contactPhone("9876543213")
                .contactEmail("suresh.patil@hospital.com")
                .build();
        dutyOfficerRepository.save(officer);

        System.out.println("=== Sample data loaded successfully ===");
        System.out.println("Logins: nurse1, nurse2, incharge1, officer1 — password: password123");
    }
}

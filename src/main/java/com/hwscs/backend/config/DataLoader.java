package com.hwscs.backend.config;

import com.hwscs.backend.entity.*;
import com.hwscs.backend.enums.NurseType;
import com.hwscs.backend.enums.Role;
import com.hwscs.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final NurseRepository nurseRepository;
    private final ShiftRepository shiftRepository;
    private final NurseShiftRepository nurseShiftRepository;
    private final NursingInchargeRepository nursingInchargeRepository;
    private final DutyOfficerRepository dutyOfficerRepository;
    private final PasswordEncoder passwordEncoder;
    private final String password = "Password@12345";

    public DataLoader(DepartmentRepository departmentRepository, UserRepository userRepository,
                      NurseRepository nurseRepository, ShiftRepository shiftRepository, NurseShiftRepository nurseShiftRepository,
                      NursingInchargeRepository nursingInchargeRepository, DutyOfficerRepository dutyOfficerRepository,
                      PasswordEncoder passwordEncoder) {
        super();
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.nurseRepository = nurseRepository;
        this.shiftRepository = shiftRepository;
        this.nurseShiftRepository = nurseShiftRepository;
        this.nursingInchargeRepository = nursingInchargeRepository;
        this.dutyOfficerRepository = dutyOfficerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (departmentRepository.count() > 0)
            return;

        // ─────────────────────────────────────────────
        // 1. DEPARTMENTS (multiple for realism)
        // ─────────────────────────────────────────────
        Department icu = createDepartment("ICU", "Block A - Floor 2");
        Department emergency = createDepartment("Emergency", "Block B - Ground Floor");
        Department generalWard = createDepartment("General Ward", "Block C - Floor 1");

        // ─────────────────────────────────────────────
        // 2. SHIFTS
        // ─────────────────────────────────────────────
        Shift morning = createShift("Morning", 6, 0, 14, 0);
        Shift evening = createShift("Evening", 14, 0, 22, 0);
        Shift night = createShift("Night", 22, 0, 6, 0);

        List<Shift> shifts = List.of(morning, evening, night);

        // ─────────────────────────────────────────────
        // 3. USERS + NURSES (BULK GENERATION)
        // ─────────────────────────────────────────────
        List<Nurse> allNurses = new ArrayList<>();

        allNurses.addAll(generateNurses("ICU-N", icu, 5));
        allNurses.addAll(generateNurses("ER-N", emergency, 5));
        allNurses.addAll(generateNurses("GW-N", generalWard, 5));

        // ─────────────────────────────────────────────
        // 4. SHIFT ASSIGNMENTS (ROTATION LOGIC)
        // ─────────────────────────────────────────────
        LocalDate baseDate = LocalDate.now().minusDays(1);

        List<NurseShift> assignments = new ArrayList<>();

        for (int i = 0; i < allNurses.size(); i++) {
            Nurse nurse = allNurses.get(i);

            for (int d = 0; d < 3; d++) {
                Shift shift = shifts.get((i + d) % shifts.size());

                assignments.add(NurseShift.builder().nurse(nurse).shift(shift).shiftDate(baseDate.plusDays(d)).build());
            }
        }

        nurseShiftRepository.saveAll(assignments);

        // ─────────────────────────────────────────────
        // 5. INCHARGE + DUTY OFFICER PER DEPT
        // ─────────────────────────────────────────────
        createIncharge("incharge.icu", icu, "Dr. Meera Joshi", "NI001");
        createIncharge("incharge.er", emergency, "Dr. Rahul Mehta", "NI002");
        createIncharge("incharge.gw", generalWard, "Dr. Kavita Shah", "NI003");

        createDutyOfficer("officer.icu", icu, "DO001", "Suresh Patil");
        createDutyOfficer("officer.er", emergency, "DO002", "Amit Deshmukh");

        System.out.println("\n=== SAMPLE DATA LOADED ===");
        System.out.println("Departments: ICU, Emergency, General Ward");
        System.out.println("Users created per department with shift rotations");
        System.out.println("Default password: " + password);
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────

    private Department createDepartment(String name, String location) {
        Department dept = Department.builder().name(name).location(location).build();
        return departmentRepository.save(dept);
    }

    private Shift createShift(String name, int sh, int sm, int eh, int em) {
        Shift shift = Shift.builder().shiftName(name).startTime(LocalTime.of(sh, sm)).endTime(LocalTime.of(eh, em))
                .build();
        return shiftRepository.save(shift);
    }

    private List<Nurse> generateNurses(String prefix, Department dept, int count) {

        List<Nurse> nurses = new ArrayList<>();

        for (int i = 1; i <= count; i++) {

            String username = prefix.toLowerCase() + i;

            User user = userRepository.save(User.builder().username(username)
                    .password(passwordEncoder.encode(password)).firstLogin(true).role(Role.NURSE).department(dept).build());

            Nurse nurse = nurseRepository.save(Nurse.builder().user(user).department(dept)
                    .employeeCode(prefix + String.format("%03d", i)).fullName("Nurse " + username.toUpperCase())
                    .nurseType(i % 2 == 0 ? NurseType.CONTRACTUAL : NurseType.PERMANENT)
                    .contactPhone("98765" + (10000 + i)).contactEmail(username + "@hospital.com").build());

            nurses.add(nurse);
        }

        return nurses;
    }

    private void createIncharge(String username, Department dept, String name, String code) {

        User user = userRepository.save(User.builder().username(username)
                .password(passwordEncoder.encode(password)).firstLogin(true).role(Role.NURSING_INCHARGE).department(dept).build());

        nursingInchargeRepository.save(NursingIncharge.builder().user(user).department(dept).employeeCode(code)
                .fullName(name).contactPhone("9000000000").contactEmail(username + "@hospital.com").build());
    }

    private void createDutyOfficer(String username, Department dept, String code, String name) {

        User user = userRepository.save(User.builder().username(username)
                .password(passwordEncoder.encode(password)).firstLogin(true).role(Role.DUTY_OFFICER).department(dept).build());

        dutyOfficerRepository.save(DutyOfficer.builder().user(user).department(dept).employeeCode(code).fullName(name)
                .contactPhone("9111111111").contactEmail(username + "@hospital.com").build());
    }
}
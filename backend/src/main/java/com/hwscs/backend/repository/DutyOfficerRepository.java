package com.hwscs.backend.repository;

import com.hwscs.backend.entity.DutyOfficer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DutyOfficerRepository extends JpaRepository<DutyOfficer, Integer> {

    Optional<DutyOfficer> findByUser_Id(Integer userId);

    Optional<DutyOfficer> findByUser_Username(String username);

    Optional<DutyOfficer> findByEmployeeCode(String employeeCode);

    Optional<DutyOfficer> findByContactPhone(String contactPhone);

    Optional<DutyOfficer> findByContactEmail(String contactEmail);
}

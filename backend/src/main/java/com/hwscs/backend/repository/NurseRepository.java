package com.hwscs.backend.repository;

import com.hwscs.backend.entity.Department;
import com.hwscs.backend.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NurseRepository extends JpaRepository<Nurse, Integer> {

    List<Nurse> findByDepartment(Department department);

    Optional<Nurse> findByUser_Id(Integer userId);

    Optional<Nurse> findByUser_Username(String username);

    Optional<Nurse> findByEmployeeCode(String employeeCode);

    Optional<Nurse> findByContactPhone(String contactPhone);

    Optional<Nurse> findByContactEmail(String contactEmail);
}

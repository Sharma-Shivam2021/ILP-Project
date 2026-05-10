package com.hwscs.backend.repository;

import com.hwscs.backend.entity.Department;
import com.hwscs.backend.entity.NursingIncharge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NursingInchargeRepository extends JpaRepository<NursingIncharge, Integer> {

    Optional<NursingIncharge> findByUser_Id(Integer userId);

    Optional<NursingIncharge> findByUser_Username(String username);

    Optional<NursingIncharge> findByDepartment(Department department);
}

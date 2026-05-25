package com.hwscs.backend.repository;

import com.hwscs.backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUser_Username(String username);
    Optional<Admin> findByEmployeeCode(String employeeCode);
    Optional<Admin> findByContactPhone(String contactPhone);
    Optional<Admin> findByContactEmail(String contactEmail);
}

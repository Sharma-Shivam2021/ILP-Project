package com.hwscs.backend.service.interfaces;

import com.hwscs.backend.dto.requests.RegisterRequestDto;
import com.hwscs.backend.dto.response.AdminUserResponseDto;
import com.hwscs.backend.entity.AuditLog;
import com.hwscs.backend.entity.Department;

import java.util.List;
import java.util.Map;

public interface AdminService {

    List<AdminUserResponseDto> getAllUsers();

    void updateUser(Long userId, RegisterRequestDto dto, String adminUsername);

    void deleteUser(Long userId, String adminUsername);

    void resetPassword(Long userId, String newPassword, String adminUsername);

    List<Department> getAllDepartments();

    Department createDepartment(Department department, String adminUsername);

    Department updateDepartment(Integer id, Department department, String adminUsername);

    void deleteDepartment(Integer id, String adminUsername);

    List<AuditLog> getAuditLogs();

    Map<String, Object> getStats();
}

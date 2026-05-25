package com.hwscs.backend.controller;

import com.hwscs.backend.dto.requests.RegisterRequestDto;
import com.hwscs.backend.dto.response.AdminUserResponseDto;
import com.hwscs.backend.entity.AuditLog;
import com.hwscs.backend.entity.Department;
import com.hwscs.backend.service.interfaces.AdminService;
import com.hwscs.backend.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;

    public AdminController(AdminService adminService, AuthService authService) {
        this.adminService = adminService;
        this.authService = authService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterRequestDto dto) {
        authService.register(dto);
        return ResponseEntity.ok(Map.of("message", "User created successfully"));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                         @Valid @RequestBody RegisterRequestDto dto,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        adminService.updateUser(id, dto, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        adminService.deleteUser(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @PutMapping("/users/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id,
                                            @RequestBody Map<String, String> body,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
        }
        if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Password must be at least 8 characters and contain uppercase, lowercase, number, and special characters."));
        }
        adminService.resetPassword(id, newPassword, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(adminService.getAllDepartments());
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(adminService.createDepartment(department, userDetails.getUsername()));
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Integer id,
                                                       @RequestBody Department department,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(adminService.updateDepartment(id, department, userDetails.getUsername()));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Integer id,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        adminService.deleteDepartment(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Department deleted successfully"));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(adminService.getAuditLogs());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }
}

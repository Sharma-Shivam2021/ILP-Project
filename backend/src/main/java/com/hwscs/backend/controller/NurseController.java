package com.hwscs.backend.controller;

import com.hwscs.backend.dto.response.NurseResponseDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;
import com.hwscs.backend.service.interfaces.NurseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses")
public class NurseController {

    private final NurseService nurseService;

    public NurseController(NurseService nurseService) {
        super();
        this.nurseService = nurseService;
    }

    // NURSE: view own profile
    @GetMapping("/me")
    public ResponseEntity<NurseResponseDto> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(nurseService.getMyProfile(userDetails.getUsername()));
    }

    // NURSE: view own shift assignments
    @GetMapping("/me/shifts")
    public ResponseEntity<List<NurseShiftResponseDto>> getMyShifts(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(nurseService.getMyShifts(userDetails.getUsername()));
    }

    // NURSING_INCHARGE / DUTY_OFFICER: list nurses in a department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<NurseResponseDto>> getNursesByDepartment(
            @PathVariable Integer departmentId) {
        return ResponseEntity.ok(nurseService.getNursesByDepartment(departmentId));
    }

    // NURSING_INCHARGE / DUTY_OFFICER: view a specific nurse
    @GetMapping("/{nurseId}")
    public ResponseEntity<NurseResponseDto> getNurseById(@PathVariable Integer nurseId) {
        return ResponseEntity.ok(nurseService.getNurseById(nurseId));
    }

    // NURSING_INCHARGE / ADMIN: toggle Allow Shift Change permission
    @PutMapping("/{nurseId}/allow-shift-change")
    public ResponseEntity<NurseResponseDto> updateAllowShiftChange(
            @PathVariable Integer nurseId,
            @RequestParam Boolean allow,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(nurseService.updateAllowShiftChange(nurseId, allow, userDetails.getUsername()));
    }
}
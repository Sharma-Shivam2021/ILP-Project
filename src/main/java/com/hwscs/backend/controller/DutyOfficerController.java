package com.hwscs.backend.controller;

import com.hwscs.backend.dto.response.DepartmentStaffingDto;
import com.hwscs.backend.dto.response.NurseResponseDto;
import com.hwscs.backend.dto.response.ShiftRequestResponseDto;
import com.hwscs.backend.service.DutyOfficerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/duty-officer")
@RequiredArgsConstructor
public class DutyOfficerController {

    private final DutyOfficerService dutyOfficerService;

    // Daily staffing report: who is assigned, how many are unassigned
    @GetMapping("/staffing-report")
    public ResponseEntity<DepartmentStaffingDto> getDailyStaffingReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate reportDate = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(
                dutyOfficerService.getDailyStaffingReport(userDetails.getUsername(), reportDate));
    }

    // List all nurses in the duty officer's department
    @GetMapping("/nurses")
    public ResponseEntity<List<NurseResponseDto>> getDepartmentNurses(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                dutyOfficerService.getDepartmentNurses(userDetails.getUsername()));
    }

    // View all shift requests in the department (read-only monitoring)
    @GetMapping("/shift-requests")
    public ResponseEntity<List<ShiftRequestResponseDto>> getDepartmentShiftRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                dutyOfficerService.getDepartmentShiftRequests(userDetails.getUsername()));
    }
}

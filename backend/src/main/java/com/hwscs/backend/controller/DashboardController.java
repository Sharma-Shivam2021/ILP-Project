package com.hwscs.backend.controller;

import com.hwscs.backend.dto.response.DutyOfficerDashboardDto;
import com.hwscs.backend.dto.response.InchargeDashboardDto;
import com.hwscs.backend.dto.response.NurseDashboardDto;
import com.hwscs.backend.service.interfaces.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        super();
        this.dashboardService = dashboardService;
    }

    @GetMapping("/nurse")
    public ResponseEntity<NurseDashboardDto> getNurseDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(dashboardService.getNurseDashboard(userDetails.getUsername()));
    }

    @GetMapping("/incharge")
    public ResponseEntity<InchargeDashboardDto> getInchargeDashboardDto(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(dashboardService.getInchargeDashboard(userDetails.getUsername()));
    }

    @GetMapping("/duty-officer")
    public ResponseEntity<DutyOfficerDashboardDto> getDutyOfficerDashboardDto(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(dashboardService.getDutyOfficerDashboard(userDetails.getUsername()));
    }

}
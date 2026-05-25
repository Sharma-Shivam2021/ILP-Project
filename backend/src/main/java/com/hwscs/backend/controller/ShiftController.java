package com.hwscs.backend.controller;

import com.hwscs.backend.dto.requests.AssignShiftDto;
import com.hwscs.backend.dto.requests.CreateShiftDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;
import com.hwscs.backend.dto.response.ShiftResponseDto;
import com.hwscs.backend.service.interfaces.ShiftService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        super();
        this.shiftService = shiftService;
    }

    // NURSING_INCHARGE / DUTY_OFFICER: assign a shift to a nurse
    @PostMapping("/assign")
    public ResponseEntity<NurseShiftResponseDto> assignShift(@Valid @RequestBody AssignShiftDto dto) {
        return ResponseEntity.ok(shiftService.assignShift(dto));
    }

    // NURSING_INCHARGE / DUTY_OFFICER: view department schedule for a given date
    @GetMapping("/department/{departmentId}/schedule")
    public ResponseEntity<List<NurseShiftResponseDto>> getDepartmentSchedule(@PathVariable Integer departmentId,
                                                                             @RequestParam LocalDate date) {
        return ResponseEntity.ok(shiftService.getDepartmentSchedule(departmentId, date));
    }

    @PostMapping
    public ResponseEntity<ShiftResponseDto> createShift(@Valid @RequestBody CreateShiftDto dto,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(shiftService.createShift(dto, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<ShiftResponseDto>> getAllShifts() {
        return ResponseEntity.ok(shiftService.getAllShifts());
    }
}
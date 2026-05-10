package com.hwscs.backend.controller;

import com.hwscs.backend.dto.request.AssignShiftDto;
import com.hwscs.backend.dto.response.NurseShiftResponseDto;
import com.hwscs.backend.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    // NURSING_INCHARGE / DUTY_OFFICER: assign a shift to a nurse
    @PostMapping("/assign")
    public ResponseEntity<NurseShiftResponseDto> assignShift(
            @Valid @RequestBody AssignShiftDto dto) {
        return ResponseEntity.ok(shiftService.assignShift(dto));
    }

    // NURSING_INCHARGE / DUTY_OFFICER: view department schedule for a given date
    @GetMapping("/department/{departmentId}/schedule")
    public ResponseEntity<List<NurseShiftResponseDto>> getDepartmentSchedule(
            @PathVariable Integer departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(shiftService.getDepartmentSchedule(departmentId, date));
    }
}

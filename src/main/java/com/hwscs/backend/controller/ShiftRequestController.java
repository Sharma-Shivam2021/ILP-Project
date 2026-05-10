package com.hwscs.backend.controller;

import com.hwscs.backend.dto.request.CreateShiftRequestDto;
import com.hwscs.backend.dto.request.InchargeApprovalDto;
import com.hwscs.backend.dto.response.PeerResponseDto;
import com.hwscs.backend.dto.response.ShiftRequestResponseDto;
import com.hwscs.backend.service.ShiftRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shift-requests")
@RequiredArgsConstructor
public class ShiftRequestController {

    private final ShiftRequestService shiftRequestService;

    // NURSE: create a new shift swap request
    @PostMapping
    public ResponseEntity<ShiftRequestResponseDto> createRequest(
            @Valid @RequestBody CreateShiftRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                shiftRequestService.createRequest(dto, userDetails.getUsername()));
    }

    // NURSE (peer): accept or reject a request directed at them
    @PutMapping("/peer-response")
    public ResponseEntity<ShiftRequestResponseDto> peerResponse(
            @Valid @RequestBody PeerResponseDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                shiftRequestService.peerResponse(dto, userDetails.getUsername()));
    }

    // NURSING_INCHARGE: approve or reject a peer-accepted request
    @PutMapping("/incharge-review")
    public ResponseEntity<ShiftRequestResponseDto> inchargeReview(
            @Valid @RequestBody InchargeApprovalDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                shiftRequestService.inchargeReview(dto, userDetails.getUsername()));
    }

    // NURSE: view all requests they are involved in (as requester or peer)
    @GetMapping("/my-requests")
    public ResponseEntity<List<ShiftRequestResponseDto>> getMyRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                shiftRequestService.getMyRequests(userDetails.getUsername()));
    }

    // NURSING_INCHARGE: view all peer-accepted requests awaiting their review
    @GetMapping("/pending-review")
    public ResponseEntity<List<ShiftRequestResponseDto>> getPendingReview(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                shiftRequestService.getPendingInchargeReview(userDetails.getUsername()));
    }

    // Any authenticated role: view a single request by ID
    @GetMapping("/{id}")
    public ResponseEntity<ShiftRequestResponseDto> getRequestById(@PathVariable Integer id) {
        return ResponseEntity.ok(shiftRequestService.getRequestById(id));
    }
}

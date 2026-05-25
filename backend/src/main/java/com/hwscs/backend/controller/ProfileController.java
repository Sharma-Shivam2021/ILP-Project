package com.hwscs.backend.controller;

import com.hwscs.backend.dto.requests.ChangePasswordDto;
import com.hwscs.backend.dto.requests.UpdateProfileDto;
import com.hwscs.backend.service.interfaces.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        super();
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(profileService.getMyProfile(userDetails.getUsername()));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UpdateProfileDto dto) {
        return ResponseEntity.ok(profileService.updateMyProfile(userDetails.getUsername(), dto));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ChangePasswordDto dto) {
        profileService.changePassword(userDetails.getUsername(), dto);
        return ResponseEntity.ok("Password changed successfully");
    }


}
package com.hwscs.backend.controller;

import com.hwscs.backend.dto.requests.LoginRequestDto;
import com.hwscs.backend.dto.requests.RegisterRequestDto;
import com.hwscs.backend.dto.response.LoginResponseDto;
import com.hwscs.backend.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        super();
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto dto) {
        authService.register(dto);
        return ResponseEntity.ok(java.util.Map.of("message", "Registration successful. You can now log in."));
    }
}
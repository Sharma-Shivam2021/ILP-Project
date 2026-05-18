package com.hwscs.backend.service.impl;

import com.hwscs.backend.dto.requests.LoginRequestDto;
import com.hwscs.backend.dto.response.LoginResponseDto;
import com.hwscs.backend.entity.User;
import com.hwscs.backend.exception.InvalidRequestException;
import com.hwscs.backend.repository.UserRepository;
import com.hwscs.backend.service.interfaces.AuthService;
import com.hwscs.backend.service.interfaces.LoginAttemptService;
import com.hwscs.backend.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                           UserRepository userRepository, JwtUtil jwtUtil, LoginAttemptService loginAttemptService) {
        super();
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidRequestException("Invalid Username or Password"));

        if (user.getAccountLocked()) {
            boolean unlocked = loginAttemptService.unlockWhenTimeExpired(user);
            if (!unlocked) {
                throw new InvalidRequestException("Account temporarily locked. Try again later");
            }
        }

        try {
            // Throws BadCredentialsException if invalid — caught by GlobalExceptionHandler
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            loginAttemptService.loginSucceeded(user);
        } catch (Exception e) {
            loginAttemptService.loginFailed(user);
            throw new InvalidRequestException("Invalid Username or Password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return LoginResponseDto.builder().token(token).username(user.getUsername()).firstLogin(user.getFirstLogin()).role(user.getRole().name())
                .userId(user.getId()).build();
    }
}
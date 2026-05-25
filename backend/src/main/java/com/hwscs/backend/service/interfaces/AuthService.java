package com.hwscs.backend.service.interfaces;

import com.hwscs.backend.dto.requests.LoginRequestDto;
import com.hwscs.backend.dto.response.LoginResponseDto;

import com.hwscs.backend.dto.requests.RegisterRequestDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto dto);

    void register(RegisterRequestDto dto);
}

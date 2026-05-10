package com.hwscs.backend.service;

import com.hwscs.backend.dto.request.LoginRequestDto;
import com.hwscs.backend.dto.response.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto dto);
}

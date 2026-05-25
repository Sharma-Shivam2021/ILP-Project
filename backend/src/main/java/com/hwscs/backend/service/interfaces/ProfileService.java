package com.hwscs.backend.service.interfaces;

import com.hwscs.backend.dto.requests.ChangePasswordDto;
import com.hwscs.backend.dto.requests.UpdateProfileDto;

public interface ProfileService {
    Object getMyProfile(String username);

    Object updateMyProfile(String username, UpdateProfileDto dto);

    void changePassword(String username, ChangePasswordDto dto);
}
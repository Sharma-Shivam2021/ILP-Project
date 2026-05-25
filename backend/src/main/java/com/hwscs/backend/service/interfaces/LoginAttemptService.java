package com.hwscs.backend.service.interfaces;

import com.hwscs.backend.entity.User;

public interface LoginAttemptService {
    void loginSucceeded(User user);

    void loginFailed(User user);

    boolean unlockWhenTimeExpired(User user);
}
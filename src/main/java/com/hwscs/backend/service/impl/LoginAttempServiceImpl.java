package com.hwscs.backend.service.impl;

import com.hwscs.backend.entity.User;
import com.hwscs.backend.repository.UserRepository;
import com.hwscs.backend.service.interfaces.LoginAttemptService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class LoginAttempServiceImpl implements LoginAttemptService {

    private static final int MAX_ATTEMPTS = 4;
    private static final long LOCK_DURATION_SECONDS = 30;

    private final UserRepository userRepository;

    public LoginAttempServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public void loginSucceeded(User user) {
        user.setFailedAttempts(0);
        user.setAccountLocked(false);
        user.setLockTime(null);

    }

    @Override
    public void loginFailed(User user) {
        int attempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(attempts);
        if (attempts >= MAX_ATTEMPTS) {
            user.setAccountLocked(true);
            user.setLockTime(LocalDateTime.now());
        }
        userRepository.save(user);
    }

    @Override
    public boolean unlockWhenTimeExpired(User user) {
        if (user.getLockTime() == null) {
            return false;
        }
        long seconds = ChronoUnit.SECONDS.between(user.getLockTime(), LocalDateTime.now());
        if (seconds >= LOCK_DURATION_SECONDS) {
            user.setFailedAttempts(0);
            user.setAccountLocked(false);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
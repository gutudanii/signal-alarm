package com.alarm.signal.user.repository;

import com.alarm.signal.user.model.PasswordResetToken;
import com.alarm.signal.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, java.util.UUID> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
}


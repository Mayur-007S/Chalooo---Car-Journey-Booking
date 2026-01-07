package com.api.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.api.model.PasswordResetToken;
import com.api.model.User;
import com.api.repository.PasswordResetTokenRepository;
import com.api.repository.UserRepository;
import com.api.service.PasswordResetService;
import com.api.mail.service.MailService;

import jakarta.mail.MessagingException;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final MailService mailService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final Logger log = LoggerFactory.getLogger(PasswordResetServiceImpl.class);

    public PasswordResetServiceImpl(UserRepository userRepository, PasswordResetTokenRepository tokenRepository,
            MailService mailService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void processForgotPassword(String email) {
        log.info("Processing forgot password for email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("User not found with email: {}", email);
            throw new RuntimeException("User not found with email: " + email);
        }

        // Create or update token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = tokenRepository.findByUser(user).orElse(new PasswordResetToken());

        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        tokenRepository.save(resetToken);
        log.info("Saved reset token for user: {}", email);

        try {
            mailService.sendForgotPasswordEmail(email, token);
        } catch (MessagingException e) {
            log.error("Failed to send forgot password email to {}", email, e);
            throw new RuntimeException("Error sending email. Please try again later.");
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        if (token != null)
            token = token.trim();
        log.info("Attempting to reset password with token: [{}]", token);

        final String searchToken = token;
        PasswordResetToken resetToken = tokenRepository.findByToken(searchToken)
                .orElseThrow(() -> {
                    log.error("Token [{}] not found in database.", searchToken);
                    return new RuntimeException("Invalid or expired token");
                });

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete the token after successful reset
        tokenRepository.delete(resetToken);
    }
}

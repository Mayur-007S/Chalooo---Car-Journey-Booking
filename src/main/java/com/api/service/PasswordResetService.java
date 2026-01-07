package com.api.service;

public interface PasswordResetService {
    void processForgotPassword(String email);

    void resetPassword(String token, String newPassword);
}

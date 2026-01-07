# Forgot Password Feature - Chaloo Ride Sharing

This document explains the implementation of the **Forgot Password** feature in the Chaloo project.

## Overview
The Forgot Password feature allows users to recover their accounts by resetting their passwords via a secure email link.

## Components Used

### 1. Model: `PasswordResetToken`
- **Purpose**: Stores the unique reset token associated with a user.
- **Fields**:
    - `id`: Auto-incremented primary key.
    - `token`: A randomly generated UUID string.
    - `user`: A one-to-one relationship with the `User` entity.
    - `expiryDate`: A timestamp (set to 1 hour from creation) after which the token is invalid.

### 2. DTOs (Data Transfer Objects)
- **`ForgotPasswordRequest`**: Used to capture the user's email address during the initial request.
- **`ResetPasswordRequest`**: Used to capture the reset token and the new password.

### 3. Repository: `PasswordResetTokenRepository`
- Provides methods to find tokens by the token string or by the user.

### 4. Service: `PasswordResetService` & `PasswordResetServiceImpl`
- **`processForgotPassword(email)`**:
    - Validates if the user exists.
    - Generates a UUID token.
    - Saves/Updates the token in the database.
    - Sends an email using `MailService`.
- **`resetPassword(token, newPassword)`**:
    - Validates the token and checks for expiration.
    - Encodes the new password using `BCryptPasswordEncoder`.
    - Updates the user's password and deletes the used token.

### 5. Controller: `PasswordResetController`
- **`POST /api/v1/user/forgot-password`**: Endpoint to trigger the reset process.
- **`POST /api/v1/user/reset-password`**: Endpoint to actually update the password using the token.

### 6. Mail Integration: `MailService`
- **`sendForgotPasswordEmail(to, token)`**:
    - Constructs a reset link.
    - Uses a Thymeleaf template (`AuthEmailTemplate/forgotPassword.html`) to generate a professional-looking email.
    - Sends the email asynchronously.

### 7. Security Configuration: `SecurityConfig`
- Exposed `BCryptPasswordEncoder` as a Spring Bean for reuse.
- Updated the security filter chain to permit public access to the forgot-password and reset-password endpoints.

## How to Test
1. **Request Reset**: Send a POST request to `/api/v1/user/forgot-password` with the body:
   ```json
   {
       "email": "user@example.com"
   }
   ```
2. **Check Email**: The user will receive an email with a link like:
   `http://localhost:8080/api/v1/user/reset-password?token=uuid-token-here`
3. **Reset Password**: Send a POST request to `/api/v1/user/reset-password` with the body:
   ```json
   {
       "token": "uuid-token-here",
       "newPassword": "yourNewStrongPassword123"
   }
   ```

## Technologies & Libraries
- **Spring Boot**: Core framework.
- **Spring Data JPA**: For database interactions.
- **Spring Security**: For password encoding and endpoint protection.
- **Java Mail Sender & Thymeleaf**: For sending rich HTML emails.
- **Lombok**: To reduce boilerplate code.
- **Jakarta Validation**: For input validation.

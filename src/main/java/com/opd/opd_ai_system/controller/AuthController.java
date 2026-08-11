package com.opd.opd_ai_system.controller;

import com.opd.opd_ai_system.dto.*;
import com.opd.opd_ai_system.service.PasswordResetService;
import com.opd.opd_ai_system.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;

    public AuthController(
            UserService userService,
            PasswordResetService passwordResetService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody OtpRequest request) {
        return passwordResetService.sendOtp(request.getEmail());
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody VerifyOtpRequest request) {
        return passwordResetService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );
    }

    // NOTE: now takes the OTP too, not just email + newPassword. Without
    // it, this endpoint could reset any account's password given only the
    // email — verify-otp being a separate call doesn't stop someone from
    // skipping straight to this one. See PasswordResetService.resetPassword,
    // which re-checks the OTP server-side before allowing the change.
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        return passwordResetService.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );
    }
}
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

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        return passwordResetService.resetPassword(
                request.getEmail(),
                request.getNewPassword()
        );
    }
}
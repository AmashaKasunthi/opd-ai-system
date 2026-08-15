
package com.opd.opd_ai_system.controller;

import com.opd.opd_ai_system.dto.*;
import com.opd.opd_ai_system.service.AdminService;
import com.opd.opd_ai_system.service.PasswordResetService;
import com.opd.opd_ai_system.service.StaffPasswordResetService;
import com.opd.opd_ai_system.service.UserService;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final AdminService adminService;
    private final PasswordResetService passwordResetService;
    @Getter
    private final StaffPasswordResetService staffPasswordResetService;

    public AuthController(
            UserService userService,
            AdminService adminService,
            PasswordResetService passwordResetService,
            StaffPasswordResetService staffPasswordResetService) {
        this.userService = userService;
        this.adminService = adminService;
        this.passwordResetService = passwordResetService;
        this.staffPasswordResetService =  staffPasswordResetService;
    }

    // This is the Admin Console login (AdminLogin.jsx) — admin accounts
    // live in the separate "admin" table, not "users", so this must go
    // through AdminService, not UserService.
    @PostMapping("/login")
    public AdminLoginResponse login(@RequestBody AdminLoginRequest request) {
        return adminService.login(request);
    }

    // This is the Doctor/Nurse login (Login.jsx) — those accounts live in
    // the "users" table with a role, so this is a SEPARATE endpoint from
    // admin login above. One endpoint can't serve both tables — that was
    // the earlier bug when everything pointed at "/login".
    @PostMapping("/staff-login")
    public LoginResponse staffLogin(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody OtpRequest request) {
        return passwordResetService.sendOtp(request.getEmail());
    }

    @PostMapping("/staff/forgot-password")
    public String staffForgotPassword(@RequestBody OtpRequest request) {
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
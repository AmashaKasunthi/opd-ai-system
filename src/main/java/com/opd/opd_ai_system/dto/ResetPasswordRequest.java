package com.opd.opd_ai_system.dto;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {

    private String email;
    private String otp;
    private String newPassword;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
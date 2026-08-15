package com.opd.opd_ai_system.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPasswordRequest {

    private String email;
    private String otp;
    private String newPassword;

}
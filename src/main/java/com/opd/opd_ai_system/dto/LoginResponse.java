package com.opd.opd_ai_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class LoginResponse {
    private Integer userId;
    private String message;
    private String role;

    public LoginResponse() {

    }
}

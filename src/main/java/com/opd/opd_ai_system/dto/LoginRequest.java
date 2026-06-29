package com.opd.opd_ai_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginRequest {

    private String email;
    private String password;
    private String role;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

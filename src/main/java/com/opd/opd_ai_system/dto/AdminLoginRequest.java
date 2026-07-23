package com.opd.opd_ai_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginRequest {

    private String email;
    private String password;

}
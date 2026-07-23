package com.opd.opd_ai_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminLoginResponse {

    private Integer adminId;
    private String fullName;
    private String message;

}
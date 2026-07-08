package com.opd.opd_ai_system.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIPredictionResponse {

    private String disease;

    private String riskLevel;

    private String precautions;

}
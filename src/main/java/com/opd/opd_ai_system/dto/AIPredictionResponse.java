package com.opd.opd_ai_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AIPredictionResponse {

    private String predictedDisease;

    private String riskLevel;

    private String description;

    private List<String> precautions;

}
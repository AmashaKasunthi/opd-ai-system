package com.opd.opd_ai_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class MonthlyReportDTO {

    private int totalPatients;
    private int totalMedicalRecords;

    private long lowRisk;
    private long mediumRisk;
    private long highRisk;

    private Map<String, Long> diseaseCount;

}
package com.opd.opd_ai_system.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class AnnualReportDTO {

    private int totalPatients;

    private int totalMedicalRecords;

    private long lowRisk;

    private long mediumRisk;

    private long highRisk;

    private Map<String, Long> diseaseCount;

    private Map<String, Long> monthlyRecords;

}

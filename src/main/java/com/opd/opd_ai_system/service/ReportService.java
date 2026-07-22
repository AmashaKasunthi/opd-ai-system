package com.opd.opd_ai_system.service;

import com.opd.opd_ai_system.dto.AnnualReportDTO;
import com.opd.opd_ai_system.dto.MonthlyReportDTO;
import com.opd.opd_ai_system.entity.MedicalRecord;
import com.opd.opd_ai_system.repository.MedicalRecordRepository;
import com.opd.opd_ai_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    public MonthlyReportDTO getMonthlyReport(int year, int month){

        LocalDateTime start =
                YearMonth.of(year,month)
                        .atDay(1)
                        .atStartOfDay();

        LocalDateTime end =
                YearMonth.of(year,month)
                        .atEndOfMonth()
                        .atTime(23,59,59);

        List<MedicalRecord> records =
                medicalRecordRepository.findByConsultationDateBetween(start, end);
        MonthlyReportDTO dto = new MonthlyReportDTO();

        dto.setTotalPatients(
                (int) patientRepository.count()
        );

        dto.setTotalMedicalRecords(
                records.size()
        );

        dto.setLowRisk(
                records.stream()
                        .filter(r->"Low".equalsIgnoreCase(r.getRiskLevel()))
                        .count()
        );

        dto.setMediumRisk(
                records.stream()
                        .filter(r->"Medium".equalsIgnoreCase(r.getRiskLevel()))
                        .count()
        );

        dto.setHighRisk(
                records.stream()
                        .filter(r->"High".equalsIgnoreCase(r.getRiskLevel()))
                        .count()
        );

        Map<String, Long> diseases =
                records.stream()
                        .filter(r -> r.getPredictedDisease() != null)
                        .collect(Collectors.groupingBy(
                                MedicalRecord::getPredictedDisease,
                                Collectors.counting()
                        ));

        dto.setDiseaseCount(diseases);

        return dto;

    }
    public AnnualReportDTO getAnnualReport(int year) {

        LocalDateTime start =
                LocalDateTime.of(year, 1, 1, 0, 0);

        LocalDateTime end =
                LocalDateTime.of(year, 12, 31, 23, 59, 59);

        List<MedicalRecord> records =
                medicalRecordRepository.findByConsultationDateBetween(start, end);

        AnnualReportDTO dto = new AnnualReportDTO();

        dto.setTotalPatients((int) patientRepository.count());

        dto.setTotalMedicalRecords(records.size());

        dto.setLowRisk(
                records.stream()
                        .filter(r -> "Low".equalsIgnoreCase(r.getRiskLevel()))
                        .count()
        );

        dto.setMediumRisk(
                records.stream()
                        .filter(r -> "Medium".equalsIgnoreCase(r.getRiskLevel()))
                        .count()
        );

        dto.setHighRisk(
                records.stream()
                        .filter(r -> "High".equalsIgnoreCase(r.getRiskLevel()))
                        .count()
        );

        // Disease Count
        Map<String, Long> diseaseCount =
                records.stream()
                        .filter(r -> r.getPredictedDisease() != null)
                        .collect(Collectors.groupingBy(
                                MedicalRecord::getPredictedDisease,
                                Collectors.counting()
                        ));

        dto.setDiseaseCount(diseaseCount);

        // Monthly Records
        Map<String, Long> monthlyRecords =
                records.stream()
                        .collect(Collectors.groupingBy(
                                r -> r.getConsultationDate()
                                        .getMonth()
                                        .toString(),
                                Collectors.counting()
                        ));

        dto.setMonthlyRecords(monthlyRecords);

        return dto;
    }
}
package com.opd.opd_ai_system.service;
import com.opd.opd_ai_system.dto.AIPredictionResponse;
import com.opd.opd_ai_system.entity.MedicalRecord;
import com.opd.opd_ai_system.entity.Patient;
import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.repository.MedicalRecordRepository;
import com.opd.opd_ai_system.repository.PatientRepository;
import com.opd.opd_ai_system.repository.UserRepository;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AIService aiService;

    // Add record
    public MedicalRecord addRecord(
            Integer patientId,
            Integer userId,
            MedicalRecord record){

        Patient patient = patientRepository.findById(patientId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if(patient == null || user == null){
            return null;
        }

        record.setPatient(patient);
        record.setCreatedBy(user);

        // AI Prediction
        AIPredictionResponse ai = aiService.predict(record.getSymptoms());

        record.setPredictedDisease(ai.getPredictedDisease());
        record.setRiskLevel(ai.getRiskLevel());

        record.setPrecautions(
                String.join("\n", ai.getPrecautions())
        );

        return medicalRecordRepository.save(record);
    }

    // Get all records
    public List<MedicalRecord> getAllRecords(){
        return medicalRecordRepository.findAll();
    }

    // ============================================================
    // GET ALL MEDICAL RECORDS FOR ONE PATIENT
    // ============================================================

    public List<MedicalRecord> getAllRecordsByPatient(
            Integer patientId) {

        return medicalRecordRepository
                .findByPatientPatientIdOrderByRecordIdDesc(patientId);
    }


    // ============================================================
    // GET ONE MEDICAL RECORD BY ID
    // ============================================================

    public MedicalRecord getRecordById(Integer id) {

        return medicalRecordRepository
                .findById(id)
                .orElse(null);
    }

    // Update record
    public MedicalRecord updateRecord(
            Integer id,
            MedicalRecord updatedRecord) {

        MedicalRecord record =
                medicalRecordRepository.findById(id).orElse(null);

        if (record == null) {
            return null;
        }

        // Update medical details
        record.setSymptoms(updatedRecord.getSymptoms());
        record.setBloodPressure(updatedRecord.getBloodPressure());
        record.setTemperature(updatedRecord.getTemperature());
        record.setHeartRate(updatedRecord.getHeartRate());
        record.setWeight(updatedRecord.getWeight());
        record.setNotes(updatedRecord.getNotes());

        // Call AI again using updated symptoms
        AIPredictionResponse ai = aiService.predict(record.getSymptoms());

        // Update AI prediction fields
        record.setPredictedDisease(ai.getPredictedDisease());
        record.setRiskLevel(ai.getRiskLevel());

        if (ai.getPrecautions() != null && !ai.getPrecautions().isEmpty()) {
            record.setPrecautions(String.join("\n", ai.getPrecautions()));
        } else {
            record.setPrecautions("");
        }

        // Save updated record
        return medicalRecordRepository.save(record);
    }

    // Delete record
    public String deleteRecord(Integer id){

        MedicalRecord record =
                medicalRecordRepository.findById(id).orElse(null);

        if(record != null){
            medicalRecordRepository.delete(record);
            return "Medical record deleted successfully";
        }

        return "Medical record not found";
    }
    // Get latest medical record by patient
    public MedicalRecord getLatestRecordByPatient(Integer patientId) {

        return medicalRecordRepository
                .findTopByPatientPatientIdOrderByRecordIdDesc(patientId);

    }
}
package com.opd.opd_ai_system.service;
import com.opd.opd_ai_system.entity.MedicalRecord;
import com.opd.opd_ai_system.entity.Patient;
import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.repository.MedicalRecordRepository;
import com.opd.opd_ai_system.repository.PatientRepository;
import com.opd.opd_ai_system.repository.UserRepository;

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

        return medicalRecordRepository.save(record);
    }

    // Get all records
    public List<MedicalRecord> getAllRecords(){
        return medicalRecordRepository.findAll();
    }

    // Get record by ID
    public MedicalRecord getRecordById(Integer id){
        return medicalRecordRepository.findById(id).orElse(null);
    }

    // Update record
    public MedicalRecord updateRecord(
            Integer id,
            MedicalRecord updatedRecord){

        MedicalRecord record =
                medicalRecordRepository.findById(id).orElse(null);

        if(record != null){

            record.setSymptoms(updatedRecord.getSymptoms());
            record.setBloodPressure(updatedRecord.getBloodPressure());
            record.setTemperature(updatedRecord.getTemperature());
            record.setHeartRate(updatedRecord.getHeartRate());
            record.setDiagnosis(updatedRecord.getDiagnosis());
            record.setRiskLevel(updatedRecord.getRiskLevel());

            return medicalRecordRepository.save(record);
        }

        return null;
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
}
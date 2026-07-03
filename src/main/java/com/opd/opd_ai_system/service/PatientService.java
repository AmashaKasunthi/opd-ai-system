package com.opd.opd_ai_system.service;


import com.opd.opd_ai_system.entity.Patient;
import com.opd.opd_ai_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Add patient
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // View all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // View single patient
    public Patient getPatientById(Integer id) {
        return patientRepository.findById(id).orElse(null);
    }

    // Update patient
    public Patient updatePatient(Integer id, Patient patientDetails) {

        Patient patient = patientRepository.findById(id).orElse(null);

        if (patient != null) {
            patient.setPatientNum(patientDetails.getPatientNum());
            patient.setFullName(patientDetails.getFullName());
            patient.setAge(patientDetails.getAge());
            patient.setGender(patientDetails.getGender());
            patient.setContactNumber(patientDetails.getContactNumber());
            patient.setAddress(patientDetails.getAddress());

            return patientRepository.save(patient);
        }

        return null;
    }

    // Delete patient
    public String deletePatient(Integer id) {

        Patient patient = patientRepository.findById(id).orElse(null);

        if (patient != null) {
            patientRepository.delete(patient);
            return "Patient deleted successfully";
        }

        return "Patient not found";
    }

    public List<Patient> getRecentPatients() {
        return patientRepository.findTop5ByOrderByCreatedAtDesc();
    }
}
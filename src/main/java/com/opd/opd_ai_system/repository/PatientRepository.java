package com.opd.opd_ai_system.repository;

import com.opd.opd_ai_system.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
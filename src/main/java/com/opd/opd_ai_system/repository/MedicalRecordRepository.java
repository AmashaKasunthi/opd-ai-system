package com.opd.opd_ai_system.repository;


import com.opd.opd_ai_system.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
}
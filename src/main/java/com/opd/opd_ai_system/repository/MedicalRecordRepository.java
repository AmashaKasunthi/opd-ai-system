package com.opd.opd_ai_system.repository;


import com.opd.opd_ai_system.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
    MedicalRecord findTopByPatientPatientIdOrderByRecordIdDesc(Integer patientId);
    List<MedicalRecord> findByConsultationDateBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}
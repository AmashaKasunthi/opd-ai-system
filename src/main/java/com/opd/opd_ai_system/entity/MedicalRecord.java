package com.opd.opd_ai_system.entity;

import jakarta.persistence.*;
        import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.List;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    private String symptoms;

    private String bloodPressure;

    private Double temperature;

    private Integer heartRate;

    private String notes;

    private Integer weight;

    // AI Result

    private String predictedDisease;

    private String riskLevel;

    @Column(length = 2000)
    private String precautions;

    @CreationTimestamp
    @Column(name = "consultation_date", updatable = false)
    private LocalDateTime consultationDate;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}

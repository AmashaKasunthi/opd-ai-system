package com.opd.opd_ai_system.entity;

import jakarta.persistence.*;
        import lombok.*;

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
}
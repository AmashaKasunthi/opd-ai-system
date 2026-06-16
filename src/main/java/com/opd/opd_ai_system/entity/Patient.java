package com.opd.opd_ai_system.entity;

import jakarta.persistence.*;
        import lombok.*;


@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;
    private String patientNum;

    private String fullName;

    private Integer age;

    private String gender;

    private String contactNumber;

    private String address;
}
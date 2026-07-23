package com.opd.opd_ai_system.entity;

import jakarta.persistence.*;
        import lombok.*;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    private String contactNumber;
}
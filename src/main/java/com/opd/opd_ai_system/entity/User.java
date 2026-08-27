package com.opd.opd_ai_system.entity;

import jakarta.persistence.*;
        import lombok.*;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer userId;

    private String fullName;

    @Column(unique=true)
    private String email;

    private String password;

    private String role;

    private String specialization;

    private Integer contactNumber;


}
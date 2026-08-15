package com.opd.opd_ai_system.service;

import com.opd.opd_ai_system.dto.LoginRequest;
import com.opd.opd_ai_system.dto.LoginResponse;
import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    // ============================
    // Doctor / Nurse Login
    // ============================
    public LoginResponse login(LoginRequest request) {

        // Check email and role
        User user = userRepository
                .findByEmailAndRole(
                        request.getEmail(),
                        request.getRole()
                )
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or role")
                );

        // Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException("Invalid password");
        }

        // Login successful
        LoginResponse response = new LoginResponse();

        response.setUserId(user.getUserId());
        response.setMessage("Login Successful");
        response.setRole(user.getRole());

        return response;
    }
}
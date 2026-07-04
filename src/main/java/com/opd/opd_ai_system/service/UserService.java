package com.opd.opd_ai_system.service;

import com.opd.opd_ai_system.dto.LoginRequest;
import com.opd.opd_ai_system.dto.LoginResponse;
import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserService {

    @Autowired
    UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmailAndRole(
                        request.getEmail(),
                        request.getRole())
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getUserId());
        response.setMessage("Login Successful");
        response.setRole(user.getRole());

        return response;
    }

}
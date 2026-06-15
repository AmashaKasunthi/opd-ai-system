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

    public LoginResponse login(LoginRequest request){

        User user=userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if(user==null){

            return new LoginResponse(
                    "User not found",
                    null
            );

        }

        if(!user.getPassword()
                .equals(request.getPassword())){

            return new LoginResponse(
                    "Wrong password",
                    null
            );

        }

        return new LoginResponse(
                "Login Successful",
                user.getRole()
        );

    }

}
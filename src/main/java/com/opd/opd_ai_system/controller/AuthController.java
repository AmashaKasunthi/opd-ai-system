package com.opd.opd_ai_system.controller;

import com.opd.opd_ai_system.dto.LoginRequest;
import com.opd.opd_ai_system.dto.LoginResponse;
import com.opd.opd_ai_system.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/login")

    public LoginResponse login(
            @RequestBody LoginRequest request){

        return userService.login(request);

    }

}
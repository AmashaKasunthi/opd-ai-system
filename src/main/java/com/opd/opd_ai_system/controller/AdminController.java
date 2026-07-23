package com.opd.opd_ai_system.controller;

import com.opd.opd_ai_system.dto.AdminLoginRequest;
import com.opd.opd_ai_system.dto.AdminLoginResponse;
import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ==========================
    // Admin Login
    // ==========================
    @PostMapping("/login")
    public AdminLoginResponse login(@RequestBody AdminLoginRequest request) {
        return adminService.login(request);
    }

    // ==========================
    // Add Doctor
    // ==========================
    @PostMapping("/doctor")
    public User addDoctor(@RequestBody User user) {
        user.setRole("DOCTOR");
        return adminService.saveUser(user);
    }

    // ==========================
    // Add Nurse
    // ==========================
    @PostMapping("/nurse")
    public User addNurse(@RequestBody User user) {
        user.setRole("NURSE");
        return adminService.saveUser(user);
    }

    // ==========================
    // View All Users
    // ==========================
    @GetMapping("/users")
    public List<User> getUsers() {
        return adminService.getUsers();
    }

    // ==========================
    // View Single User
    // ==========================
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Integer id) {
        return adminService.getUser(id);
    }

    // ==========================
    // Update User
    // ==========================
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id,
                           @RequestBody User user) {
        return adminService.updateUser(id, user);
    }

    // ==========================
    // Delete User
    // ==========================
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Integer id) {
        adminService.deleteUser(id);
        return "User deleted successfully";
    }
}
package com.opd.opd_ai_system.service;

import com.opd.opd_ai_system.dto.AdminLoginRequest;
import com.opd.opd_ai_system.dto.AdminLoginResponse;
import com.opd.opd_ai_system.entity.Admin;
import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.repository.AdminRepository;
import com.opd.opd_ai_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    // ===========================
    // Admin Login
    // ===========================
    public AdminLoginResponse login(AdminLoginRequest request){

        Admin admin = adminRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid Email"));

        if(!admin.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        return new AdminLoginResponse(
                admin.getAdminId(),
                admin.getFullName(),
                "Login Successful"
        );
    }

    // ===========================
    // Add Doctor / Nurse
    // ===========================
    public User saveUser(User user){
        return userRepository.save(user);
    }

    // ===========================
    // View All Users
    // ===========================
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    // ===========================
    // View One User
    // ===========================
    public User getUser(Integer id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ===========================
    // Update User
    // ===========================
    public User updateUser(Integer id, User updatedUser){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRole(updatedUser.getRole());
        user.setSpecialization(updatedUser.getSpecialization());
        user.setContactNumber(updatedUser.getContactNumber());

        return userRepository.save(user);
    }

    // ===========================
    // Delete User
    // ===========================
    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }
}
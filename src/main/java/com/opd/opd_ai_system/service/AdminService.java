package com.opd.opd_ai_system.service;

import com.opd.opd_ai_system.dto.AdminLoginRequest;
import com.opd.opd_ai_system.dto.AdminLoginResponse;
import com.opd.opd_ai_system.entity.Admin;
import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.repository.AdminRepository;
import com.opd.opd_ai_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ===========================
    // Admin Login
    // ===========================
    public AdminLoginResponse login(AdminLoginRequest request){

        Admin admin = adminRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid Email"));

        // Use matches() to compare the raw password against the bcrypt
        // hash — a plain .equals() check will never succeed against a
        // hashed password, since the hash is a one-way encoding, not
        // the original text.
        if(!passwordEncoder.matches(request.getPassword(), admin.getPassword())){
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
    public User saveUser(User user) {

        // Check duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException(
                    "A user with this email already exists."
            );
        }

        // Validate role
        if (!"DOCTOR".equals(user.getRole())
                && !"NURSE".equals(user.getRole())) {

            throw new RuntimeException(
                    "Invalid user role."
            );
        }

        // Validate password
        if (user.getPassword() == null
                || user.getPassword().isBlank()) {

            throw new RuntimeException(
                    "Password is required."
            );
        }

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

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

        // Only touch the password if a new one was actually submitted —
        // and encode it, same as saveUser(). Without this check, an edit
        // form that leaves the password field blank would overwrite the
        // real password with an empty (or plain-text) value.
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

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
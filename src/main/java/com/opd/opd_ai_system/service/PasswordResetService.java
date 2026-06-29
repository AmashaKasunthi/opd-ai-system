package com.opd.opd_ai_system.service;

import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // store OTP temporarily (simple version)
    private Map<String, String> otpStorage = new HashMap<>();

    // STEP 1: send OTP
    public String sendOtp(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "User not found";
        }

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        otpStorage.put(email, otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OPD Password Reset OTP");
        message.setText("Your OTP is: " + otp);

        mailSender.send(message);

        return "OTP sent successfully";
    }

    // STEP 2: verify OTP
    public String verifyOtp(String email, String otp) {

        if (!otpStorage.containsKey(email)) {
            return "OTP expired or not found";
        }

        if (otpStorage.get(email).equals(otp)) {
            return "OTP verified";
        }

        return "Invalid OTP";
    }

    // STEP 3: reset password
    public String resetPassword(String email, String newPassword) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "User not found";
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        otpStorage.remove(email);

        return "Password reset successful";
    }
}
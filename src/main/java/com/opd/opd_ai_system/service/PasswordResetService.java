package com.opd.opd_ai_system.service;

import com.opd.opd_ai_system.entity.Admin;
import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.repository.AdminRepository;
import com.opd.opd_ai_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    private static final long OTP_TTL_MILLIS = 10 * 60 * 1000;
    private static final int MAX_ATTEMPTS = 5;

    private final Map<String, OtpEntry> otpStorage =
            new ConcurrentHashMap<>();

    private static class OtpEntry {

        String otp;
        long expiresAt;
        int attempts;

        OtpEntry(String otp, long expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
            this.attempts = 0;
        }

        boolean isExpired() {
            return Instant.now().toEpochMilli() > expiresAt;
        }
    }

    // =====================================================
    // STEP 1 - SEND OTP
    // =====================================================

    public String sendOtp(String email) {

        // First check Admin table
        Optional<Admin> admin =
                adminRepository.findByEmail(email);

        // Then check Users table
        Optional<User> user =
                userRepository.findByEmail(email);

        if (admin.isPresent() || user.isPresent()) {

            String otp = String.valueOf(
                    100000 + new Random().nextInt(900000)
            );

            long expiresAt =
                    Instant.now().toEpochMilli()
                            + OTP_TTL_MILLIS;

            otpStorage.put(
                    email,
                    new OtpEntry(otp, expiresAt)
            );

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(email);
            message.setSubject(
                    "OPD AI System - Password Reset OTP"
            );

            message.setText(
                    "Your password reset OTP is: "
                            + otp
                            + "\n\n"
                            + "This OTP will expire in 10 minutes."
                            + "\n\n"
                            + "If you did not request a password reset, "
                            + "please ignore this email."
            );

            try {

                mailSender.send(message);

                System.out.println(
                        "OTP sent successfully to: " + email
                );

            } catch (Exception ex) {

                System.err.println(
                        "Failed to send OTP: "
                                + ex.getMessage()
                );

                ex.printStackTrace();

                // Remove OTP if email failed
                otpStorage.remove(email);

                throw new RuntimeException(
                        "Unable to send OTP email."
                );
            }
        }

        // Generic response
        return "If that email is registered, an OTP has been sent.";
    }

    // =====================================================
    // STEP 2 - VERIFY OTP
    // =====================================================

    public String verifyOtp(
            String email,
            String otp) {

        OtpEntry entry =
                otpStorage.get(email);

        if (entry == null) {
            return "OTP expired or not found";
        }

        if (entry.isExpired()) {

            otpStorage.remove(email);

            return "OTP expired or not found";
        }

        if (entry.attempts >= MAX_ATTEMPTS) {

            otpStorage.remove(email);

            return "Too many attempts. Request a new OTP.";
        }

        if (entry.otp.equals(otp)) {

            return "OTP verified";
        }

        entry.attempts++;

        return "Invalid OTP";
    }

    // =====================================================
    // STEP 3 - RESET PASSWORD
    // =====================================================

    public String resetPassword(
            String email,
            String otp,
            String newPassword) {

        OtpEntry entry =
                otpStorage.get(email);

        if (entry == null || entry.isExpired()) {

            otpStorage.remove(email);

            return "Unable to reset password. Please request a new OTP.";
        }

        if (entry.attempts >= MAX_ATTEMPTS) {

            otpStorage.remove(email);

            return "Unable to reset password. Please request a new OTP.";
        }

        if (!entry.otp.equals(otp)) {

            entry.attempts++;

            return "Unable to reset password. Please request a new OTP.";
        }

        if (newPassword == null ||
                newPassword.length() < 8) {

            return "Password must be at least 8 characters.";
        }

        // =================================================
        // CHECK ADMIN
        // =================================================

        Optional<Admin> admin =
                adminRepository.findByEmail(email);

        if (admin.isPresent()) {

            Admin adminEntity = admin.get();

            adminEntity.setPassword(
                    encoder.encode(newPassword)
            );

            adminRepository.save(adminEntity);

            otpStorage.remove(email);

            return "Password reset successful";
        }

        // =================================================
        // CHECK DOCTOR / NURSE
        // =================================================

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isPresent()) {

            User userEntity = user.get();

            userEntity.setPassword(
                    encoder.encode(newPassword)
            );

            userRepository.save(userEntity);

            otpStorage.remove(email);

            return "Password reset successful";
        }

        return "Unable to reset password.";
    }
}
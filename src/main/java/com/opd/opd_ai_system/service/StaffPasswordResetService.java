package com.opd.opd_ai_system.service;

import com.opd.opd_ai_system.entity.User;
import com.opd.opd_ai_system.repository.UserRepository;
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
public class StaffPasswordResetService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    private static final long OTP_TTL_MILLIS = 10 * 60 * 1000;
    private static final int MAX_ATTEMPTS = 5;

    private final Map<String, OtpEntry> otpStorage =
            new ConcurrentHashMap<>();

    public StaffPasswordResetService(
            UserRepository userRepository,
            JavaMailSender mailSender) {

        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

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

    // =========================================================
    // STEP 1 - SEND OTP
    // =========================================================

    public String sendOtp(String email) {

        Optional<User> userOptional =
                userRepository.findByEmail(email);

        // Only Doctor and Nurse accounts are allowed
        if (userOptional.isEmpty()) {
            return "If that email is registered, an OTP has been sent.";
        }

        User user = userOptional.get();

        String role = user.getRole();

        if (role == null ||
                (!role.equalsIgnoreCase("DOCTOR")
                        && !role.equalsIgnoreCase("NURSE"))) {

            return "If that email is registered, an OTP has been sent.";
        }

        // Generate 6-digit OTP
        String otp = String.valueOf(
                100000 + new Random().nextInt(900000)
        );

        long expiresAt =
                Instant.now().toEpochMilli() + OTP_TTL_MILLIS;

        otpStorage.put(
                email,
                new OtpEntry(otp, expiresAt)
        );

        // Email
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("OPD System - Password Reset OTP");

        message.setText(
                "Hello " + user.getFullName() + ",\n\n" +
                        "Your password reset verification code is:\n\n" +
                        otp + "\n\n" +
                        "This code will expire in 10 minutes.\n\n" +
                        "If you did not request a password reset, " +
                        "please ignore this email.\n\n" +
                        "OPD Decision Support System"
        );

        try {

            mailSender.send(message);

            System.out.println(
                    "Password reset OTP sent to: " + email
            );

        } catch (Exception ex) {

            System.err.println(
                    "Failed to send OTP email: "
                            + ex.getMessage()
            );

            ex.printStackTrace();

            // Remove OTP because email was not sent
            otpStorage.remove(email);

            throw new RuntimeException(
                    "Unable to send password reset email."
            );
        }

        return "If that email is registered, an OTP has been sent.";
    }

    // =========================================================
    // STEP 2 - VERIFY OTP
    // =========================================================

    public String verifyOtp(String email, String otp) {

        OtpEntry entry = otpStorage.get(email);

        if (entry == null) {
            throw new RuntimeException(
                    "OTP expired or not found."
            );
        }

        if (entry.isExpired()) {

            otpStorage.remove(email);

            throw new RuntimeException(
                    "OTP expired. Please request a new code."
            );
        }

        if (entry.attempts >= MAX_ATTEMPTS) {

            otpStorage.remove(email);

            throw new RuntimeException(
                    "Too many attempts. Please request a new OTP."
            );
        }

        if (!entry.otp.equals(otp)) {

            entry.attempts++;

            throw new RuntimeException(
                    "Invalid OTP."
            );
        }

        return "OTP verified";
    }

    // =========================================================
    // STEP 3 - RESET PASSWORD
    // =========================================================

    public String resetPassword(
            String email,
            String otp,
            String newPassword) {

        Optional<User> userOptional =
                userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {

            throw new RuntimeException(
                    "Unable to reset password."
            );
        }

        User user = userOptional.get();

        // Only Doctor/Nurse
        String role = user.getRole();

        if (role == null ||
                (!role.equalsIgnoreCase("DOCTOR")
                        && !role.equalsIgnoreCase("NURSE"))) {

            throw new RuntimeException(
                    "Unable to reset password."
            );
        }

        OtpEntry entry = otpStorage.get(email);

        if (entry == null || entry.isExpired()) {

            otpStorage.remove(email);

            throw new RuntimeException(
                    "OTP expired. Please request a new OTP."
            );
        }

        if (entry.attempts >= MAX_ATTEMPTS) {

            otpStorage.remove(email);

            throw new RuntimeException(
                    "Too many attempts. Please request a new OTP."
            );
        }

        if (!entry.otp.equals(otp)) {

            entry.attempts++;

            throw new RuntimeException(
                    "Invalid OTP."
            );
        }

        if (newPassword == null ||
                newPassword.length() < 8) {

            throw new RuntimeException(
                    "Password must be at least 8 characters."
            );
        }

        // Hash new password
        user.setPassword(
                encoder.encode(newPassword)
        );

        userRepository.save(user);

        // OTP can no longer be reused
        otpStorage.remove(email);

        return "Password reset successful";
    }
}
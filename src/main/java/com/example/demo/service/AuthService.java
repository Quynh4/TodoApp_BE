package com.example.demo.service;


import com.example.demo.DTO.*;
import com.example.demo.entity.OTP;
import com.example.demo.entity.User;
import com.example.demo.repository.OTPRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Value("${otp.expiration}")
    private long otpExpiration;

    public ApiResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getUsername());

        if (userOpt.isEmpty()) {
            return new ApiResponse(false, "User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ApiResponse(false, "Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new ApiResponse(true, "Login successful", token);
    }

    public ApiResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return new ApiResponse(false, "Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse(false, "Email already exists");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);
        return new ApiResponse(true, "User registered successfully");
    }

    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return new ApiResponse(false, "Email not found");
        }

        User user = userOpt.get();

        // Generate 6-digit OTP
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        // Delete any existing OTP for this user
        otpRepository.findByUsernameAndVerifiedTrue(user.getUsername())
                .ifPresent(otp -> otpRepository.delete(otp));

        // Save new OTP
        OTP otp = new OTP(user.getUsername(), otpCode);
        otpRepository.save(otp);

        // Send email
        try {
            emailService.sendOTP(request.getEmail(), otpCode);
            return new ApiResponse(true, "OTP sent to your email");
        } catch (Exception e) {
            return new ApiResponse(false, "Failed to send email");
        }
    }

    public ApiResponse verifyOTP(VerifyOTPRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return new ApiResponse(false, "Email not found");
        }

        User user = userOpt.get();
        Optional<OTP> otpOpt = otpRepository.findByUsernameAndVerifiedFalse(user.getUsername());

        if (otpOpt.isEmpty()) {
            return new ApiResponse(false, "No valid OTP found");
        }

        OTP otp = otpOpt.get();

        // Check if OTP is expired
        if (otp.getRequestedAt().plusSeconds(otpExpiration / 1000).isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            return new ApiResponse(false, "OTP has expired");
        }

        // Check if OTP is locked due to too many attempts
        if (otp.isLocked()) {
            return new ApiResponse(false, "OTP is locked due to too many attempts");
        }

        // Verify OTP
        if (!otp.getCode().equals(request.getOtp())) {
            otp.setAttempts(otp.getAttempts() + 1);
            if (otp.getAttempts() >= 3) {
                otp.setLocked(true);
            }
            otpRepository.save(otp);
            return new ApiResponse(false, "Invalid OTP");
        }

        otp.setVerified(true);
        otpRepository.save(otp);
        return new ApiResponse(true, "OTP verified successfully");
    }

    public ApiResponse resetPassword(ResetPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return new ApiResponse(false, "Email not found");
        }

        User user = userOpt.get();
        Optional<OTP> otpOpt = otpRepository.findByUsernameAndVerifiedTrue(user.getUsername());

        if (otpOpt.isEmpty()) {
            return new ApiResponse(false, "No verified OTP found");
        }

        OTP otp = otpOpt.get();

        if (!otp.isVerified() || !otp.getCode().equals(request.getOtp())) {
            return new ApiResponse(false, "Invalid or unverified OTP");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Delete used OTP
        otpRepository.delete(otp);

        return new ApiResponse(true, "Password reset successfully");
    }
}
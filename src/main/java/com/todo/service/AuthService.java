package com.todo.service;


import com.todo.DTO.*;
import com.todo.entity.OTP;
import com.todo.entity.User;
import com.todo.repository.OTPRepository;
import com.todo.repository.UserRepository;
import com.todo.security.JwtUtil;
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

    public ApiResponseDTO login(LoginRequestDTO request) {
        //ngoc fix code
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return new ApiResponseDTO(false, "User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ApiResponseDTO(false, "Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new ApiResponseDTO(true, "Login successful", token);
    }

    public ApiResponseDTO signup(SignupRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return new ApiResponseDTO(false, "Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponseDTO(false, "Email already exists");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);
        return new ApiResponseDTO(true, "User registered successfully");
    }

    public ApiResponseDTO forgotPassword(ForgotPasswordRequestDTO request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return new ApiResponseDTO(false, "Email not found");
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
            return new ApiResponseDTO(true, "OTP sent to your email");
        } catch (Exception e) {
            return new ApiResponseDTO(false, "Failed to send email");
        }
    }

    public ApiResponseDTO verifyOTP(VerifyOTPRequestDTO request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return new ApiResponseDTO(false, "Email not found");
        }

        User user = userOpt.get();
        Optional<OTP> otpOpt = otpRepository.findByUsernameAndVerifiedFalse(user.getUsername());

        if (otpOpt.isEmpty()) {
            return new ApiResponseDTO(false, "No valid OTP found");
        }

        OTP otp = otpOpt.get();

        // Check if OTP is expired
        if (otp.getRequestedAt().plusSeconds(otpExpiration / 1000).isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            return new ApiResponseDTO(false, "OTP has expired");
        }

        // Check if OTP is locked due to too many attempts
        if (otp.isLocked()) {
            return new ApiResponseDTO(false, "OTP is locked due to too many attempts");
        }

        // Verify OTP
        if (!otp.getCode().equals(request.getOtp())) {
            otp.setAttempts(otp.getAttempts() + 1);
            if (otp.getAttempts() >= 3) {
                otp.setLocked(true);
            }
            otpRepository.save(otp);
            return new ApiResponseDTO(false, "Invalid OTP");
        }

        otp.setVerified(true);
        otpRepository.save(otp);
        return new ApiResponseDTO(true, "OTP verified successfully");
    }

    public ApiResponseDTO resetPassword(ResetPasswordRequestDTO request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return new ApiResponseDTO(false, "Email not found");
        }

        User user = userOpt.get();
        Optional<OTP> otpOpt = otpRepository.findByUsernameAndVerifiedTrue(user.getUsername());

        if (otpOpt.isEmpty()) {
            return new ApiResponseDTO(false, "No verified OTP found");
        }

        OTP otp = otpOpt.get();

        if (!otp.isVerified() || !otp.getCode().equals(request.getOtp())) {
            return new ApiResponseDTO(false, "Invalid or unverified OTP");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Delete used OTP
        otpRepository.delete(otp);

        return new ApiResponseDTO(true, "Password reset successfully");
    }
}
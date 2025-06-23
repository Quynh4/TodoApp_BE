package com.todo.controller;
import com.todo.DTO.*;
import com.todo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        ApiResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDTO> signup(@Valid @RequestBody SignupRequestDTO request) {
        ApiResponseDTO response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        ApiResponseDTO response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDTO> verifyOTP(@Valid @RequestBody VerifyOTPRequestDTO request) {
        ApiResponseDTO response = authService.verifyOTP(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        ApiResponseDTO response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}

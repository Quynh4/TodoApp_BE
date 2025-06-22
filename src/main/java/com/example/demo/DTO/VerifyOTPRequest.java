package com.example.demo.DTO;

import jakarta.validation.constraints.NotBlank;

public class VerifyOTPRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String otp;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}

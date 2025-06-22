package com.example.demo.DTO;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String otp;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}

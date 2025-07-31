package com.techstack.techstack.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "OTP code is required")
    private String otpCode;

    public ResetPasswordRequest() {
        System.out.println("Initializing ResetPasswordRequest DTO");
    }
}

package com.techstack.techstack.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequest {
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "OTP code is required")
    private String otpCode;

    public OtpRequest() {
        System.out.println("Initializing OtpRequest DTO");
    }
}

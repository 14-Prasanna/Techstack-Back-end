package com.techstack.techstack.controller;

import com.techstack.techstack.dto.LoginRequest;
import com.techstack.techstack.dto.OtpRequest;
import com.techstack.techstack.dto.ResetPasswordRequest;
import com.techstack.techstack.dto.SignupRequest;
import com.techstack.techstack.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        System.out.println("Received signup request for: " + request.getEmail() + request.getPassword() + request.getName());
        return authService.signup(request);
    }

    @PostMapping("/signup/verify")
    public ResponseEntity<?> verifySignupOtp(@Valid @RequestBody OtpRequest request) {
        System.out.println("Received signup OTP verification request for: " + request.getEmail());
        return authService.verifySignupOtp(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        System.out.println("Received login request for: " + request.getEmail());
        return authService.login(request);
    }

    @PostMapping("/login/verify")
    public ResponseEntity<?> verifyLoginOtp(@Valid @RequestBody OtpRequest request) {
        System.out.println("Received login OTP verification request for: " + request.getEmail());
        return authService.verifyLoginOtp(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String email) {
        System.out.println("Received logout request for: " + email);
        return authService.logout(email);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        System.out.println("Received resend OTP request for: " + email);
        return authService.resendOtp(email);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestParam String email) {
        System.out.println("Received forget password request for: " + email);
        return authService.forgetPassword(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        System.out.println("Received reset password request for: " + request.getEmail());
        return authService.resetPassword(request);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestParam String email) {
        System.out.println("Received delete account request for: " + email);
        return authService.deleteAccount(email);
    }
}
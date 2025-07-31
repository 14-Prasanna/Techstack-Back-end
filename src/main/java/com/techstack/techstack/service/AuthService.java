package com.techstack.techstack.service;

import com.techstack.techstack.config.JwtUtil;
import com.techstack.techstack.dto.LoginRequest;
import com.techstack.techstack.dto.OtpRequest;
import com.techstack.techstack.dto.ResetPasswordRequest;
import com.techstack.techstack.dto.SignupRequest;
import com.techstack.techstack.entity.Otp;
import com.techstack.techstack.entity.User;
import com.techstack.techstack.repository.OtpRepository;
import com.techstack.techstack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> signup(SignupRequest request) {
        System.out.println("Processing signup for email: " + request.getEmail());
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            System.out.println("Email already exists in database: " + request.getEmail());
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setVerified(false);
        userRepository.saveUser(user);
        System.out.println("User saved as unverified: " + user.getEmail());

        String otp = generateOtp();
        saveOtp(user, otp);
        sendOtpEmail(user.getEmail(), otp);
        System.out.println("OTP sent to: " + user.getEmail());

        return ResponseEntity.ok("Please verify OTP sent to email to complete registration");
    }

    public ResponseEntity<?> verifySignupOtp(OtpRequest request) {
        System.out.println("Verifying signup OTP for email: " + request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    System.out.println("User not found in database for email: " + request.getEmail());
                    return null;
                });

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (user.isVerified()) {
            System.out.println("User already verified for email: " + request.getEmail());
            return ResponseEntity.badRequest().body("User already verified");
        }

        Otp otp = otpRepository.findOtpByUserAndCode(user, request.getOtpCode())
                .orElseGet(() -> {
                    System.out.println("Invalid OTP for user ID: " + user.getId());
                    return null;
                });

        if (otp == null || otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            System.out.println("OTP invalid or expired for user ID: " + user.getId());
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        user.setVerified(true);
        userRepository.saveUser(user);
        System.out.println("User verified and updated: " + user.getEmail());

        System.out.println("OTP verified successfully for: " + request.getEmail());
        return ResponseEntity.ok(jwtUtil.generateToken(user.getEmail()));
    }

    public ResponseEntity<?> login(LoginRequest request) {
        System.out.println("Processing login for email: " + request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    System.out.println("User not found in database for email: " + request.getEmail());
                    return null;
                });

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!user.isVerified()) {
            System.out.println("User not verified for email: " + request.getEmail());
            return ResponseEntity.badRequest().body("Please verify OTP to complete registration");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            System.out.println("Invalid password for email: " + request.getEmail());
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        String otp = generateOtp();
        saveOtp(user, otp);
        sendOtpEmail(user.getEmail(), otp);
        System.out.println("OTP sent for login to: " + user.getEmail());

        return ResponseEntity.ok("OTP sent to email for login verification");
    }

    public ResponseEntity<?> verifyLoginOtp(OtpRequest request) {
        System.out.println("Verifying login OTP for email: " + request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    System.out.println("User not found in database for email: " + request.getEmail());
                    return null;
                });

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!user.isVerified()) {
            System.out.println("User not verified for email: " + request.getEmail());
            return ResponseEntity.badRequest().body("Please verify OTP to complete registration");
        }

        Otp otp = otpRepository.findOtpByUserAndCode(user, request.getOtpCode())
                .orElseGet(() -> {
                    System.out.println("Invalid OTP for user ID: " + user.getId());
                    return null;
                });

        if (otp == null || otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            System.out.println("OTP invalid or expired for user ID: " + user.getId());
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        System.out.println("Login OTP verified for: " + request.getEmail());
        return ResponseEntity.ok(jwtUtil.generateToken(user.getEmail()));
    }

    public ResponseEntity<?> logout(String email) {
        System.out.println("Processing logout for email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    System.out.println("User not found in database for email: " + email);
                    return null;
                });

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!user.isVerified()) {
            System.out.println("User not verified for email: " + email);
            return ResponseEntity.badRequest().body("Please verify OTP to complete registration");
        }

        System.out.println("Logout successful for: " + email);
        return ResponseEntity.ok("Logged out successfully");
    }

    public ResponseEntity<?> resendOtp(String email) {
        System.out.println("Resending OTP for email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    System.out.println("User not found in database for email: " + email);
                    return null;
                });

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String otp = generateOtp();
        saveOtp(user, otp);
        sendOtpEmail(user.getEmail(), otp);
        System.out.println("New OTP sent to: " + email);

        return ResponseEntity.ok("New OTP sent to email");
    }

    public ResponseEntity<?> forgetPassword(String email) {
        System.out.println("Processing forget password for email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    System.out.println("User not found in database for email: " + email);
                    return null;
                });

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!user.isVerified()) {
            System.out.println("User not verified for email: " + email);
            return ResponseEntity.badRequest().body("Please verify OTP to complete registration");
        }

        String otp = generateOtp();
        saveOtp(user, otp);
        sendOtpEmail(user.getEmail(), otp);
        System.out.println("OTP sent for password reset to: " + email);

        return ResponseEntity.ok("OTP sent to email for password reset");
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest request) {
        System.out.println("Resetting password for email: " + request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    System.out.println("User not found in database for email: " + request.getEmail());
                    return null;
                });

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!user.isVerified()) {
            System.out.println("User not verified for email: " + request.getEmail());
            return ResponseEntity.badRequest().body("Please verify OTP to complete registration");
        }

        Otp otp = otpRepository.findOtpByUserAndCode(user, request.getOtpCode())
                .orElseGet(() -> {
                    System.out.println("Invalid OTP for user ID: " + user.getId());
                    return null;
                });

        if (otp == null || otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            System.out.println("OTP invalid or expired for user ID: " + user.getId());
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.saveUser(user);
        System.out.println("Password reset successfully for: " + request.getEmail());

        return ResponseEntity.ok("Password reset successfully");
    }

    public ResponseEntity<?> deleteAccount(String email) {
        System.out.println("Deleting account for email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    System.out.println("User not found in database for email: " + email);
                    return null;
                });

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        userRepository.deleteUserById(user.getId());
        System.out.println("Account deleted for email: " + email);

        return ResponseEntity.ok("Account deleted successfully");
    }

    private String generateOtp() {
        System.out.println("Generating new OTP");
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void saveOtp(User user, String otpCode) {
        System.out.println("Saving OTP for user ID: " + user.getId());
        Otp otp = new Otp();
        otp.setUser(user);
        otp.setOtpCode(otpCode);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.saveOtp(otp);
    }

    private void sendOtpEmail(String email, String otp) {
        System.out.println("Sending OTP email to: " + email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + ". It is valid for 5 minutes.");
        mailSender.send(message);
        System.out.println("OTP email sent successfully to: " + email);
    }
}

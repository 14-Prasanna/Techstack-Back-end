package com.techstack.techstack.repository;

import com.techstack.techstack.entity.Otp;
import com.techstack.techstack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUserAndOtpCode(User user, String otpCode);

    default void saveOtp(Otp otp) {
        System.out.println("Saving OTP for user ID: " + (otp.getUser() != null ? otp.getUser().getId() : "null"));
        save(otp);
    }

        default Optional<Otp> findOtpByUserAndCode(User user, String otpCode) {
        System.out.println("Finding OTP for user ID: " + user.getId() + " and code: " + otpCode);
        return findByUserAndOtpCode(user, otpCode);
    }
}

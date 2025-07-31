package com.techstack.techstack.controller;

import com.techstack.techstack.dto.OrderHistoryDTO;
import com.techstack.techstack.dto.UserProfileDTO;
import com.techstack.techstack.entity.User;
import com.techstack.techstack.repository.UserRepository;
import com.techstack.techstack.service.UserProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

    @GetMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderHistoryDTO>> getOrderHistory() {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(userProfileService.getUserOrders(userId));
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database: " + userEmail));
    }
}
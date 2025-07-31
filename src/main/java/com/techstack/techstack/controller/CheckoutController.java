package com.techstack.techstack.controller;

import com.techstack.techstack.dto.CheckoutRequestDTO;
import com.techstack.techstack.dto.OrderResponseDTO;
import com.techstack.techstack.entity.User;
import com.techstack.techstack.repository.UserRepository;
import com.techstack.techstack.service.CheckoutService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final UserRepository userRepository;

    /**
     * A single endpoint to handle both "Buy Now" and "Checkout from Cart".
     * The service layer will determine the flow based on the request body content.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDTO> placeOrder(@Valid @RequestBody CheckoutRequestDTO request) {
        Long userId = getAuthenticatedUserId();
        OrderResponseDTO orderResponse = checkoutService.placeOrder(userId, request);
        return ResponseEntity.ok(orderResponse);
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database: " + userEmail));
    }
}
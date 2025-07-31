package com.techstack.techstack.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToWishlistRequestDTO {
    @NotNull(message = "Product ID cannot be null")
    private Long productId;
}
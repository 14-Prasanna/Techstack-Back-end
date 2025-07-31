package com.techstack.techstack.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long id;
    private Long userId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemDTO> cartItems;

    @Data
    public static class CartItemDTO {
        private Long id;
        private Long productId;
        private String productName;
        private Integer quantity;
        private Integer price;
        private String imageUrl; // This field correctly expects a String
    }
}
package com.techstack.techstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponseDTO {
    private Long wishlistItemId; // ID of the item, needed for deletion
    private Long productId;
    private String productName;
    private Integer productPrice;
    private String productImageUrl;
    private LocalDateTime addedAt;
}
package com.techstack.techstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryDTO {
    private Long orderId;
    private String orderStatus;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private List<OrderItemSummaryDTO> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemSummaryDTO {
        private String productName;
        private String productImageUrl;
    }
}
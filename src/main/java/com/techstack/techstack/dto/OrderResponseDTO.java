package com.techstack.techstack.dto;

import com.techstack.techstack.entity.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private String orderStatus;
    private LocalDateTime orderDate;
    private String paymentMethod;
    private AddressDTO shippingAddress;
    private List<OrderItemDTO> items;
    private PriceSummaryDTO priceSummary;

    @Data
    public static class OrderItemDTO {
        private Long productId;
        private String productName;
        private int quantity;
        private int pricePerUnit;
    }

    @Data
    public static class AddressDTO {
        private String addressLine1;
        private String addressLine2;
        private String district;
        private String state;
        private String country;
        private String phoneNumber;
    }

    @Data
    public static class PriceSummaryDTO {
        private BigDecimal subtotal;
        private BigDecimal cgst;
        private BigDecimal sgst;
        private BigDecimal total;
    }
}
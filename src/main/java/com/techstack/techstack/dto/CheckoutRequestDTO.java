package com.techstack.techstack.dto;

import com.techstack.techstack.entity.Order.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequestDTO {


    private List<Long> cartItemIds;


    private Long directProductId;
    private Integer directProductQuantity;


    @NotBlank(message = "Address Line 1 is required")
    private String addressLine1;
    private String addressLine2;
    @NotBlank(message = "District is required")
    private String district;
    @NotBlank(message = "State is required")
    private String state;
    @NotBlank(message = "Country is required")
    private String country;
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15)
    private String phoneNumber;
    private String alternativePhoneNumber;


    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
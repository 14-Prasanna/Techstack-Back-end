package com.techstack.techstack.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private Integer price;
    private Integer stock;
    private byte[] imageUrl;
    private String brandName;
    private String modelName;
    private String category;
    private LocalDate manufacturingDate;
    private LocalDate deliveryDate;
    private Double rating;
    private String description;
    private SpecificationDTO specification;
    private List<ProductReviewDTO> reviews;
}
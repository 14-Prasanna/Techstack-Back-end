package com.techstack.techstack.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price; // In rupees (1L to 2L)

    @Column(nullable = false)
    private Integer stock;

    @Lob
    @Column(name = "image_data", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Column(name = "brand_name", nullable = false)
    private String brandName;

    @Column(name = "model_name")
    private String modelName;

    @Column(nullable = false)
    private String category;

    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(nullable = false)
    private Double rating;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "specification_id", nullable = false)
    private Specification specification;

    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductReview> reviews;
}
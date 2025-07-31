package com.techstack.techstack.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_reviews")
@Data
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "reviewer_name")
    private String reviewerName;

    private Integer rating;

    private String comment;
}
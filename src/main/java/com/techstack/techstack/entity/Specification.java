package com.techstack.techstack.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "specifications")
@Data
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "processor")
    private String processor;

    @Column(name = "graphics")
    private String graphics;

    @Column(name = "memory")
    private String memory;

    @Column(name = "storage")
    private String storage;

    @Column(name = "display")
    private String display;

    @Column(name = "operating_system")
    private String operatingSystem;

    @Column(name = "battery")
    private String battery;

    @Column(name = "weight")
    private String weight;

    @Column(name = "connectivity")
    private String connectivity;

    @Column(name = "key_features", columnDefinition = "TEXT")
    private String keyFeatures;
}
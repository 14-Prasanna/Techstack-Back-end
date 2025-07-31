package com.techstack.techstack.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecificationDTO {
    private Long id;

    @NotBlank(message = "Processor is mandatory")
    private String processor;

    @NotBlank(message = "Graphics is mandatory")
    private String graphics;

    @NotBlank(message = "Memory is mandatory")
    private String memory;

    @NotBlank(message = "Storage is mandatory")
    private String storage;

    @NotBlank(message = "Display is mandatory")
    private String display;

    @NotBlank(message = "Operating system is mandatory")
    private String operatingSystem;

    @NotBlank(message = "Battery is mandatory")
    private String battery;

    @NotBlank(message = "Weight is mandatory")
    private String weight;

    @NotBlank(message = "Connectivity is mandatory")
    private String connectivity;

    @NotBlank(message = "Key features are mandatory")
    private String keyFeatures;
}
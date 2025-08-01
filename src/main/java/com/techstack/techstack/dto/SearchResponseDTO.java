package com.techstack.techstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDTO {
    private List<ProductDTO> products;
    private List<String> availableBrands;
}
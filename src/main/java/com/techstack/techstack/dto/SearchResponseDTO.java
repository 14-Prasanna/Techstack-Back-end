package com.techstack.techstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A DTO to hold the results of a product search, including the
 * list of found products and a list of unique brands from those results
 * that can be used for filtering.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDTO {
    private List<ProductDTO> products;
    private List<String> availableBrands;
}
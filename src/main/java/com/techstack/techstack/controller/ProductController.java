package com.techstack.techstack.controller;

import com.techstack.techstack.dto.ProductDTO;
import com.techstack.techstack.dto.SearchResponseDTO;
import com.techstack.techstack.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class ProductController {
    private final ProductService productService;

    // --- NEW SEARCH ENDPOINT ---
    /**
     * Searches for products by a keyword and optionally filters by brand.
     * Example URL: /api/products/search?keyword=laptop
     * Example URL with filter: /api/products/search?keyword=laptop&brands=Dell,HP
     *
     * @param keyword The search term (e.g., "laptop", "samsung", "gaming").
     * @param brands  An optional comma-separated list of brands to filter by.
     * @return A response containing the matching products and a list of available brands.
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> searchProducts(
            @RequestParam(required = true) String keyword,
            @RequestParam(required = false) List<String> brands) {
        return ResponseEntity.ok(productService.searchProducts(keyword, brands));
    }

    // --- Your existing endpoints remain below ---

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Note: The create and update methods would require a way to handle
    // the byte[] image data, likely through multipart file uploads.
    // They are omitted here for brevity as they were not part of the request.
}
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


    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> searchProducts(
            @RequestParam(required = true) String keyword,
            @RequestParam(required = false) List<String> brands) {
        return ResponseEntity.ok(productService.searchProducts(keyword, brands));
    }



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
}
package com.techstack.techstack.service;

import com.techstack.techstack.dto.ProductDTO;
import com.techstack.techstack.dto.ProductReviewDTO;
import com.techstack.techstack.dto.SearchResponseDTO;
import com.techstack.techstack.dto.SpecificationDTO;
import com.techstack.techstack.entity.Product;
import com.techstack.techstack.entity.Specification;
import com.techstack.techstack.repository.ProductRepository;
import com.techstack.techstack.repository.SpecificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final SpecificationRepository specificationRepository;

    public SearchResponseDTO searchProducts(String keyword, List<String> brands) {
        List<Product> productsFoundWithoutBrandFilter = productRepository.searchByKeywordAndFilterByBrand(keyword, null);

        List<String> availableBrands = productsFoundWithoutBrandFilter.stream()
                .map(Product::getBrandName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        List<Product> finalProductList;
        if (brands != null && !brands.isEmpty()) {
            finalProductList = productsFoundWithoutBrandFilter.stream()
                    .filter(p -> brands.contains(p.getBrandName()))
                    .collect(Collectors.toList());
        } else {
            finalProductList = productsFoundWithoutBrandFilter;
        }

        List<ProductDTO> productDTOs = finalProductList.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new SearchResponseDTO(productDTOs, availableBrands);
    }

    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // This is the helper method where the correction is made
    private ProductDTO mapToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());

        // --- THIS IS THE CORRECTED LINE ---
        // The method name now matches the field 'imageUrl' in both the entity and DTO.
        productDTO.setImageUrl(product.getImageUrl());

        productDTO.setBrandName(product.getBrandName());
        productDTO.setModelName(product.getModelName());
        productDTO.setCategory(product.getCategory());
        productDTO.setDescription(product.getDescription());
        productDTO.setManufacturingDate(product.getManufacturingDate());
        productDTO.setDeliveryDate(product.getDeliveryDate());
        productDTO.setRating(product.getRating());

        if (product.getSpecification() != null) {
            SpecificationDTO specificationDTO = new SpecificationDTO();
            Specification spec = product.getSpecification();
            specificationDTO.setId(spec.getId());
            specificationDTO.setProcessor(spec.getProcessor());
            specificationDTO.setGraphics(spec.getGraphics());
            specificationDTO.setMemory(spec.getMemory());
            specificationDTO.setStorage(spec.getStorage());
            specificationDTO.setDisplay(spec.getDisplay());
            specificationDTO.setOperatingSystem(spec.getOperatingSystem());
            specificationDTO.setBattery(spec.getBattery());
            specificationDTO.setWeight(spec.getWeight());
            specificationDTO.setConnectivity(spec.getConnectivity());
            specificationDTO.setKeyFeatures(spec.getKeyFeatures());
            productDTO.setSpecification(specificationDTO);
        }

        if (product.getReviews() != null) {
            List<ProductReviewDTO> reviewDTOs = product.getReviews().stream()
                    .map(review -> {
                        ProductReviewDTO reviewDTO = new ProductReviewDTO();
                        reviewDTO.setId(review.getId());
                        reviewDTO.setProductId(review.getProduct().getId());
                        reviewDTO.setReviewerName(review.getReviewerName());
                        reviewDTO.setRating(review.getRating());
                        reviewDTO.setComment(review.getComment());
                        return reviewDTO;
                    })
                    .collect(Collectors.toList());
            productDTO.setReviews(reviewDTOs);
        }

        return productDTO;
    }
}
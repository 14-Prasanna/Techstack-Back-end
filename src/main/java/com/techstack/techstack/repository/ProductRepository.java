package com.techstack.techstack.repository;

import com.techstack.techstack.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Existing methods from your code
    List<Product> findByCategory(String category);
    List<Product> findByCategoryAndBrandName(String category, String brandName);
    List<Product> findByCategoryAndPriceBetween(String category, Integer minPrice, Integer maxPrice);
    List<Product> findByCategoryAndRatingGreaterThanEqualOrderByRatingDesc(String category, Double minRating);
    List<Product> findByCategoryAndBrandNameAndPriceBetween(String category, String brandName, Integer minPrice, Integer maxPrice);


    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.brandName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:brands IS NULL OR p.brandName IN :brands)")
    List<Product> searchByKeywordAndFilterByBrand(
            @Param("keyword") String keyword,
            @Param("brands") List<String> brands
    );
}
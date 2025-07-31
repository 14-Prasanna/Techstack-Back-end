package com.techstack.techstack.repository;

import com.techstack.techstack.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Finds all orders placed by a specific user, ordered by the creation date descending (newest first).
     */
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
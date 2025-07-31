package com.techstack.techstack.service;

import com.techstack.techstack.dto.OrderHistoryDTO;
import com.techstack.techstack.dto.UserProfileDTO;
import com.techstack.techstack.entity.Order;
import com.techstack.techstack.entity.User;
import com.techstack.techstack.repository.OrderRepository;
import com.techstack.techstack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return new UserProfileDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }

    public List<OrderHistoryDTO> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(this::mapToOrderHistoryDto)
                .collect(Collectors.toList());
    }

    private OrderHistoryDTO mapToOrderHistoryDto(Order order) {
        List<OrderHistoryDTO.OrderItemSummaryDTO> itemSummaries = order.getItems().stream()
                .map(item -> {
                    String imageUrl = null;
                    byte[] imageData = item.getProduct().getImageUrl();
                    if (imageData != null && imageData.length > 0) {
                        imageUrl = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageData);
                    }
                    return new OrderHistoryDTO.OrderItemSummaryDTO(item.getProduct().getName(), imageUrl);
                })
                .collect(Collectors.toList());

        return new OrderHistoryDTO(
                order.getId(),
                order.getOrderStatus().name(),
                order.getCreatedAt(),
                order.getTotalAmount(),
                itemSummaries
        );
    }
}
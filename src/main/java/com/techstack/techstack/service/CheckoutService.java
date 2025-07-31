package com.techstack.techstack.service;

import com.techstack.techstack.dto.CheckoutRequestDTO;
import com.techstack.techstack.dto.OrderResponseDTO;
import com.techstack.techstack.entity.*;
import com.techstack.techstack.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.05");

    @Transactional
    public OrderResponseDTO placeOrder(Long userId, CheckoutRequestDTO request) {
        boolean isDirectBuy = request.getDirectProductId() != null && request.getDirectProductQuantity() != null;
        boolean isCartBuy = request.getCartItemIds() != null && !request.getCartItemIds().isEmpty();

        if (isDirectBuy) {
            return placeOrderDirectly(userId, request);
        } else if (isCartBuy) {
            return placeOrderFromCart(userId, request);
        } else {
            throw new IllegalArgumentException("Checkout request must contain either direct product info or cart item IDs.");
        }
    }

    private OrderResponseDTO placeOrderFromCart(Long userId, CheckoutRequestDTO request) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active cart found for user"));

        List<CartItem> itemsToCheckout = cart.getCartItems().stream()
                .filter(item -> request.getCartItemIds().contains(item.getId()))
                .collect(Collectors.toList());

        if (itemsToCheckout.isEmpty()) {
            throw new RuntimeException("No valid items selected for checkout.");
        }

        Order order = createOrderFromItems(userId, request, itemsToCheckout.stream()
                .map(ci -> Map.entry(ci.getProduct(), ci.getQuantity()))
                .collect(Collectors.toList()));

        cart.getCartItems().removeAll(itemsToCheckout);
        cartItemRepository.deleteAll(itemsToCheckout);
        cartRepository.save(cart);

        return mapToDto(order);
    }

    private OrderResponseDTO placeOrderDirectly(Long userId, CheckoutRequestDTO request) {
        Product product = productRepository.findById(request.getDirectProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < request.getDirectProductQuantity()) {
            throw new RuntimeException("Not enough stock for product: " + product.getName());
        }

        Order order = createOrderFromItems(userId, request, List.of(
                Map.entry(product, request.getDirectProductQuantity())
        ));

        product.setStock(product.getStock() - request.getDirectProductQuantity());

        return mapToDto(order);
    }

    private Order createOrderFromItems(Long userId, CheckoutRequestDTO request, List<Map.Entry<Product, Integer>> productEntries) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);

        List<OrderItem> orderItems = productEntries.stream().map(entry -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(entry.getKey());
            orderItem.setQuantity(entry.getValue());
            orderItem.setPriceAtPurchase(entry.getKey().getPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setItems(orderItems);

        BigDecimal subtotal = orderItems.stream()
                .map(item -> new BigDecimal(item.getPriceAtPurchase()).multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTax = subtotal.multiply(TAX_RATE);
        order.setSubtotal(subtotal);
        order.setCgstAmount(totalTax.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
        order.setSgstAmount(totalTax.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
        order.setTotalAmount(subtotal.add(totalTax));

        order.setAddressLine1(request.getAddressLine1());
        order.setAddressLine2(request.getAddressLine2());
        order.setDistrict(request.getDistrict());
        order.setState(request.getState());
        order.setCountry(request.getCountry());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setAlternativePhoneNumber(request.getAlternativePhoneNumber());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setOrderStatus(Order.OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    private OrderResponseDTO mapToDto(Order order) {
        // This mapping logic remains the same as the previous correct version
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setOrderStatus(order.getOrderStatus().name());
        dto.setOrderDate(order.getCreatedAt());
        dto.setPaymentMethod(order.getPaymentMethod().name());

        OrderResponseDTO.AddressDTO addressDTO = new OrderResponseDTO.AddressDTO();
        addressDTO.setAddressLine1(order.getAddressLine1());
        // ... map rest of address fields
        dto.setShippingAddress(addressDTO);

        dto.setItems(order.getItems().stream().map(orderItem -> {
            OrderResponseDTO.OrderItemDTO itemDTO = new OrderResponseDTO.OrderItemDTO();
            itemDTO.setProductId(orderItem.getProduct().getId());
            itemDTO.setProductName(orderItem.getProduct().getName());
            itemDTO.setQuantity(orderItem.getQuantity());
            itemDTO.setPricePerUnit(orderItem.getPriceAtPurchase());
            return itemDTO;
        }).collect(Collectors.toList()));

        OrderResponseDTO.PriceSummaryDTO priceSummaryDTO = new OrderResponseDTO.PriceSummaryDTO();
        priceSummaryDTO.setSubtotal(order.getSubtotal());
        priceSummaryDTO.setCgst(order.getCgstAmount());
        priceSummaryDTO.setSgst(order.getSgstAmount());
        priceSummaryDTO.setTotal(order.getTotalAmount());
        dto.setPriceSummary(priceSummaryDTO);

        return dto;
    }
}
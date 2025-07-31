package com.techstack.techstack.service;

import com.techstack.techstack.dto.CartResponseDTO;
import com.techstack.techstack.entity.Cart;
import com.techstack.techstack.entity.CartItem;
import com.techstack.techstack.entity.Product;
import com.techstack.techstack.entity.User;
import com.techstack.techstack.repository.CartItemRepository;
import com.techstack.techstack.repository.CartRepository;
import com.techstack.techstack.repository.ProductRepository;
import com.techstack.techstack.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartResponseDTO addToCart(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.Status.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setStatus(Cart.Status.ACTIVE);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .ifPresentOrElse(
                        existingItem -> existingItem.setQuantity(existingItem.getQuantity() + quantity),
                        () -> {
                            Product product = productRepository.findById(productId)
                                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

                            CartItem newCartItem = new CartItem();
                            newCartItem.setCart(cart);
                            newCartItem.setProduct(product);
                            newCartItem.setQuantity(quantity);
                            newCartItem.setPrice(product.getPrice());
                            cart.getCartItems().add(newCartItem);
                        }
                );

        Cart updatedCart = cartRepository.save(cart);
        return mapToDto(updatedCart);
    }

    @Transactional
    public CartResponseDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active cart found for user"));
        return mapToDto(cart);
    }

    /**
     * CORRECTED: This is the missing method that the CartController needs.
     * It finds the user's active cart and deletes it if it exists.
     */
    @Transactional
    public void deleteCart(Long userId) {
        cartRepository.findByUserIdAndStatus(userId, Cart.Status.ACTIVE)
                .ifPresent(cart -> cartRepository.delete(cart));
    }

    @Transactional
    public CartResponseDTO removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active cart found for user"));

        CartItem itemToRemove = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!itemToRemove.getCart().getUser().getId().equals(userId)) {
            throw new SecurityException("User does not have permission to remove this item");
        }

        cart.getCartItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);

        Cart updatedCart = cartRepository.save(cart);
        return mapToDto(updatedCart);
    }

    private CartResponseDTO mapToDto(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setStatus(cart.getStatus().name());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());

        List<CartResponseDTO.CartItemDTO> itemDTOs = cart.getCartItems().stream()
                .map(item -> {
                    CartResponseDTO.CartItemDTO itemDto = new CartResponseDTO.CartItemDTO();
                    itemDto.setId(item.getId());
                    itemDto.setProductId(item.getProduct().getId());
                    itemDto.setProductName(item.getProduct().getName());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());

                    byte[] imageData = item.getProduct().getImageUrl();
                    if (imageData != null && imageData.length > 0) {
                        String base64Image = Base64.getEncoder().encodeToString(imageData);
                        itemDto.setImageUrl("data:image/jpeg;base64," + base64Image);
                    } else {
                        itemDto.setImageUrl(null);
                    }

                    return itemDto;
                }).collect(Collectors.toList());

        dto.setCartItems(itemDTOs);
        return dto;
    }
}
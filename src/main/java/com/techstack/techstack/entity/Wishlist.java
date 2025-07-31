package com.techstack.techstack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wishlists")
@Data
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A user has only one wishlist.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // This is the collection of items in the wishlist.
    @OneToMany(
            mappedBy = "wishlist",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER // Eager fetch is often okay for wishlists
    )
    private List<WishlistItem> wishlistItems = new ArrayList<>();
}
package com.techstack.techstack.repository;

import com.techstack.techstack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    default void saveUser(User user) {
        System.out.println("Saving user: " + user.getEmail());
        save(user);
    }

    default Optional<User> findUserByEmail(String email) {
        System.out.println("Finding user by email: " + email);
        return findByEmail(email);
    }

    default void deleteUserById(Long id) {
        System.out.println("Deleting user with ID: " + id);
        deleteById(id);
    }
}

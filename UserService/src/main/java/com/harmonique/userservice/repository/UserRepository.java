package com.harmonique.userservice.repository;

import com.harmonique.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for User entity.
 * Extends JpaRepository to provide basic CRUD operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by email.
     *
     * @param email Email of the user.
     * @return Optional containing User if found, empty otherwise.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a user by username.
     *
     * @param username Username of the user.
     * @return Optional containing User if found, empty otherwise.
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if a user exists by email.
     *
     * @param email Email to check.
     * @return true if user exists with the given email.
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user exists by username.
     *
     * @param username Username to check.
     * @return true if user exists with the given username.
     */
    boolean existsByUsername(String username);
}
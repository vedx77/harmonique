package com.harmonique.userservice.repository;

import com.harmonique.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    // Check if email already exists
    boolean existsByEmail(String email);

    // Check if username already exists
    boolean existsByUsername(String username);
}
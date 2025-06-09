package com.harmonique.userservice.repository;

import com.harmonique.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for Role entity.
 * Provides CRUD operations and custom query methods.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by its name.
     *
     * @param name Name of the role (e.g., "ROLE_USER", "ROLE_ADMIN").
     * @return Optional containing Role if found, empty otherwise.
     */
    Optional<Role> findByName(String name);

    /**
     * Check if a role already exists by name.
     *
     * @param name Role name to check.
     * @return true if a role exists with the given name.
     */
    boolean existsByName(String name);
}
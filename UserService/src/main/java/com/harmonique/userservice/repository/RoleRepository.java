package com.harmonique.userservice.repository;

import com.harmonique.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
    
    boolean existsByName(String name);
}

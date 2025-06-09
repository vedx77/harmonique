package com.harmonique.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Role entity representing user roles such as ROLE_USER or ROLE_ADMIN.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    @Column(nullable = false, unique = true)
    private String name; // Role name (e.g., ROLE_USER, ROLE_ADMIN)
}
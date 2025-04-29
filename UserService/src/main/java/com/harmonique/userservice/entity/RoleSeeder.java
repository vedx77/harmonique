package com.harmonique.userservice.entity;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.harmonique.userservice.repository.RoleRepository;

@Component
public class RoleSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(List.of(
                new Role(1L, "ROLE_USER"),
                new Role(2L, "ROLE_ARTIST"),
                new Role(3L, "ROLE_ADMIN")
            ));
        }
    }
}
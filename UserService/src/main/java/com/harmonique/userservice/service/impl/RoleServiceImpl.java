package com.harmonique.userservice.service.impl;

import com.harmonique.userservice.entity.Role;
import com.harmonique.userservice.payload.RoleResponse;
import com.harmonique.userservice.repository.RoleRepository;
import com.harmonique.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> RoleResponse.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .toList();
    }
}
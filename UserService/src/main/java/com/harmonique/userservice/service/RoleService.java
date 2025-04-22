package com.harmonique.userservice.service;

import com.harmonique.userservice.payload.RoleResponse;
import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();
}
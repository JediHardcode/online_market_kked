package com.gmail.derynem.service.converter;

import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.service.model.RoleDTO;

public interface RoleConverter {
    RoleDTO toDTO(Role role);

    Role toRole(RoleDTO roleDTO);
}

package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.model.role.RoleDTO;
import org.springframework.stereotype.Component;

@Component("roleConverter")
public class RoleConverterImpl implements Converter<RoleDTO, Role> {

    @Override
    public RoleDTO toDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }

    @Override
    public Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        return role;
    }
}
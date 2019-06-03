package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.service.RoleService;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.model.role.RoleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;
    private final Converter<RoleDTO, Role> roleConverter;

    public RoleServiceImpl(RoleRepository roleRepository,
                           @Qualifier("roleConverter") Converter<RoleDTO, Role> roleConverter) {
        this.roleRepository = roleRepository;
        this.roleConverter = roleConverter;
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public List<RoleDTO> getRoles() {
        List<Role> roles = roleRepository.getRoles();
        logger.info("role found, count of role is {}", roles.size());
        return roles.stream()
                .map(roleConverter::toDTO)
                .collect(Collectors.toList());
    }
}
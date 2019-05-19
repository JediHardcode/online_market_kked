package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.ProfileConverter;
import com.gmail.derynem.service.converter.RoleConverter;
import com.gmail.derynem.service.converter.UserConverter;
import com.gmail.derynem.service.model.role.RoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {
    private final RoleConverter roleConverter;
    private final ProfileConverter profileConverter;

    public UserConverterImpl(RoleConverter roleConverter, ProfileConverter profileConverter) {
        this.roleConverter = roleConverter;
        this.profileConverter = profileConverter;
    }

    @Override
    public UserDTO toDTO(User user) {
        RoleDTO roleDTO = roleConverter.toDTO(user.getRole());
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(roleDTO);
        userDTO.setPassword(user.getPassword());
        userDTO.setId(user.getId());
        userDTO.setDeleted(user.getDeleted());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setSurName(user.getSurName());
        userDTO.setMiddleName(user.getMiddleName());
        userDTO.setProfile(profileConverter.toDTO(user.getProfile()));
        return userDTO;
    }

    @Override
    public User toUser(UserDTO userDTO) {
        Role role = roleConverter.toRole(userDTO.getRole());
        User user = new User();
        user.setRole(role);
        user.setPassword(userDTO.getPassword());
        user.setId(userDTO.getId());
        user.setDeleted(userDTO.getDeleted());
        user.setSurName(userDTO.getSurName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setProfile(profileConverter.toEntity(userDTO.getProfile()));
        user.getProfile().setUser(user);
        return user;
    }

    @Override
    public User fromAddUserToUser(AddUserDTO userDTO) {
        User user = new User();
        user.setMiddleName(userDTO.getMiddleName());
        user.setSurName(userDTO.getSurName());
        user.setEmail(userDTO.getEmail());
        user.setDeleted(userDTO.getDeleted());
        Role role = new Role();
        role.setId(userDTO.getRoleId());
        user.setRole(role);
        user.setName(userDTO.getName());
        user.setProfile(profileConverter.toEntity(userDTO.getProfile()));
        user.getProfile().setUser(user);
        return user;
    }
}
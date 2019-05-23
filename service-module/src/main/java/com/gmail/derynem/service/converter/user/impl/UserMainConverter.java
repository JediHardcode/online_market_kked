package com.gmail.derynem.service.converter.user.impl;

import com.gmail.derynem.repository.model.Profile;
import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.model.profile.ProfileDTO;
import com.gmail.derynem.service.model.role.RoleDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("userMainConverter")
public class UserMainConverter implements Converter<UserDTO, User> {
    private final Converter<RoleDTO, Role> roleConverter;
    private final Converter<ProfileDTO, Profile> profileConverter;

    public UserMainConverter(@Qualifier("roleConverter") Converter<RoleDTO, Role> roleConverter,
                             @Qualifier("profileConverter") Converter<ProfileDTO, Profile> profileConverter) {
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
    public User toEntity(UserDTO userDTO) {
        Role role = roleConverter.toEntity(userDTO.getRole());
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
}
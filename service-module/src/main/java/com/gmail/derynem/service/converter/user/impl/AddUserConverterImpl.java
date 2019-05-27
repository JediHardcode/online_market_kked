package com.gmail.derynem.service.converter.user.impl;

import com.gmail.derynem.repository.model.Profile;
import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.model.profile.ProfileDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("addUserConverter")
public class AddUserConverterImpl implements Converter<AddUserDTO, User> {
    private final Converter<ProfileDTO, Profile> profileConverter;

    public AddUserConverterImpl(@Qualifier("profileConverter") Converter<ProfileDTO, Profile> profileConverter) {
        this.profileConverter = profileConverter;
    }

    @Override
    public AddUserDTO toDTO(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User toEntity(AddUserDTO userDTO) {
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
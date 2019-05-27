package com.gmail.derynem.service.converter.user.impl;

import com.gmail.derynem.repository.model.Profile;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.model.profile.ProfileDTO;
import com.gmail.derynem.service.model.user.UserCommonDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("userCommonConverter")
public class UserCommonConverterImpl implements Converter<UserCommonDTO, User> {
    private final Converter<ProfileDTO, Profile> profileConverter;

    public UserCommonConverterImpl(@Qualifier("profileConverter") Converter<ProfileDTO, Profile> profileConverter) {
        this.profileConverter = profileConverter;
    }

    @Override
    public UserCommonDTO toDTO(User user) {
        UserCommonDTO userCommonDTO = new UserCommonDTO();
        userCommonDTO.setId(user.getId());
        userCommonDTO.setMiddleName(user.getMiddleName());
        userCommonDTO.setName(user.getName());
        userCommonDTO.setSurName(user.getSurName());
        userCommonDTO.setProfileDTO(profileConverter.toDTO(user.getProfile()));
        return userCommonDTO;
    }

    @Override
    public User toEntity(UserCommonDTO userDTO) {
        throw new UnsupportedOperationException();
    }
}
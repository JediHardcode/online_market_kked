package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Profile;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.model.profile.ProfileDTO;
import org.springframework.stereotype.Component;

@Component("profileConverter")
public class ProfileConverterImpl implements Converter<ProfileDTO, Profile> {
    @Override
    public Profile toEntity(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setAddress(profileDTO.getAddress());
        profile.setTelephone(profileDTO.getTelephone());
        return profile;
    }

    @Override
    public ProfileDTO toDTO(Profile profile) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setAddress(profile.getAddress());
        profileDTO.setId(profile.getId());
        profileDTO.setTelephone(profile.getTelephone());
        return profileDTO;
    }
}
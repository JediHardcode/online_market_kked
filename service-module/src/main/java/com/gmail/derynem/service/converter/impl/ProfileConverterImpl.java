package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Profile;
import com.gmail.derynem.service.converter.ProfileConverter;
import com.gmail.derynem.service.model.profile.ProfileDTO;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverterImpl implements ProfileConverter {
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
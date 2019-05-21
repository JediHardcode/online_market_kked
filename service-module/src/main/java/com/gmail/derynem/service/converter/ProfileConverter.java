package com.gmail.derynem.service.converter;

import com.gmail.derynem.repository.model.Profile;
import com.gmail.derynem.service.model.profile.ProfileDTO;

public interface ProfileConverter {
    Profile toEntity(ProfileDTO profileDTO);

    ProfileDTO toDTO(Profile profile);
}
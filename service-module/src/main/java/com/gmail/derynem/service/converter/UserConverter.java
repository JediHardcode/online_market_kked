package com.gmail.derynem.service.converter;

import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.model.UserDTO;


public interface UserConverter {
    UserDTO toDTO(User user);

    User toUser(UserDTO userDTO);
}

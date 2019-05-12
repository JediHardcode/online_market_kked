package com.gmail.derynem.service.converter;

import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserDTO;

public interface UserConverter {
    UserDTO toDTO(User user);

    User toUser(UserDTO userDTO);

    User fromAddUserToUser(AddUserDTO userDTO);
}
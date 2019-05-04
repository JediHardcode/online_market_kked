package com.gmail.derynem.service;

import com.gmail.derynem.service.model.UserDTO;

public interface UserService {
    UserDTO getUserByEmail(String email);
}

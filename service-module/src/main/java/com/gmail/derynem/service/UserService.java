package com.gmail.derynem.service;

import com.gmail.derynem.service.model.AddUserDTO;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserByEmail(String email);

    List<UserDTO> getUsers(Integer page);

    void updateUserRole(String role, Long id);

    void deleteUsers(int[] ids);

    PageDTO getPages();

    void addUser(AddUserDTO userDTO);
}

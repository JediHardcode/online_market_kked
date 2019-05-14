package com.gmail.derynem.service;

import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserByEmail(String email);

    PageDTO<UserDTO> getUsersPageInfo(Integer page);

    void updateUserRole(UpdateRoleDTO updateRoleDTO);

    void deleteUsers(int[] ids);

    void addUser(AddUserDTO userDTO);

    void changePassword(Long id);
}
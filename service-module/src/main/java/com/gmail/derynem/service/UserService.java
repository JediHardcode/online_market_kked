package com.gmail.derynem.service;

import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserDTO;

public interface UserService {
    UserDTO getUserByEmail(String email,Boolean isDeleted);

    PageDTO<UserDTO> getUsersPageInfo(Integer page, Integer limit);

    void updateUserRole(UpdateRoleDTO updateRoleDTO) throws UserServiceException;

    void deleteUsers(Long[] ids) throws UserServiceException;

    void saveUser(AddUserDTO userDTO) throws UserServiceException;

    void updateUserInfo(UserDTO userDTO);
}
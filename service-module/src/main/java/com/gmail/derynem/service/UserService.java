package com.gmail.derynem.service;

import com.gmail.derynem.service.model.review.UpdateReviewDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.user.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserByEmail(String email);

    List<UserDTO> getUsers(Integer page);

    void updateUserRole(UpdateRoleDTO updateRoleDTO);

    void deleteUsers(int[] ids);

    PageDTO getPages();

    void addUser(AddUserDTO userDTO);

    void changePassword(Long id);
}

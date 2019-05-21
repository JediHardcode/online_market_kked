package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.RoleService;
import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.role.RoleDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import com.gmail.derynem.service.model.user.UserPrincipal;
import com.gmail.derynem.service.validation.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static com.gmail.derynem.web.constants.PageNamesConstant.*;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_PRIVATE_USERS;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_USER_PROFILE;

@Controller
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    public UserController(UserService userService,
                          RoleService roleService,
                          UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping("/private/users")
    public String showUsers(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            UpdateRoleDTO roleUpdate) {
        PageDTO<UserDTO> usersPage = userService.getUsersPageInfo(page);
        model.addAttribute("pages", usersPage.getCountOfPages());
        List<RoleDTO> roles = roleService.getRoles();
        model.addAttribute("users", usersPage.getEntities());
        model.addAttribute("roles", roles);
        model.addAttribute("userRoleUpdate", roleUpdate);
        return USERS_PAGE;
    }

    @PostMapping("/private/users/role")
    public String changeStatus(@ModelAttribute(value = "updateObj")
                               @Valid UpdateRoleDTO updateRoleDTO,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return REDIRECT_PRIVATE_USERS;
        }
        try {
            userService.updateUserRole(updateRoleDTO);
            return REDIRECT_PRIVATE_USERS;
        } catch (UserServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_PRIVATE_USERS;
        }
    }

    @PostMapping("/private/users/delete")
    public String deleteUsers(@RequestParam(value = "ids", required = false) Long[] ids) {
        if (ids == null) {
            logger.info("no selected users for delete");
            return REDIRECT_PRIVATE_USERS;
        } else {
            try {
                userService.deleteUsers(ids);
                return REDIRECT_PRIVATE_USERS;
            } catch (UserServiceException e) {
                logger.error(e.getMessage(), e);
                return REDIRECT_PRIVATE_USERS;
            }
        }
    }

    @GetMapping("/private/user/new")
    public String showAddUserPage(AddUserDTO user, Model model) {
        List<RoleDTO> roles = roleService.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return ADD_USER_PAGE;
    }

    @PostMapping("/private/user/new")
    public String addUser(@ModelAttribute(value = "user")
                          @Valid AddUserDTO user,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            List<RoleDTO> roles = roleService.getRoles();
            model.addAttribute("roles", roles);
            return ADD_USER_PAGE;
        }
        try {
            userService.saveUser(user);
            return REDIRECT_PRIVATE_USERS;
        } catch (UserServiceException e) {
            logger.error(e.getMessage());
            return REDIRECT_PRIVATE_USERS;
        }
    }

    @PostMapping("/private/user/{id}/password")
    public String changePassword(@PathVariable("id") Long id) {
        if (id == null) {
            logger.info(" user id is null");
            return REDIRECT_PRIVATE_USERS;
        }
        try {
            userService.changePassword(id);
            return REDIRECT_PRIVATE_USERS;
        } catch (UserServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_PRIVATE_USERS;
        }
    }

    @GetMapping("/public/user/profile")
    public String showUserProfile(Authentication authentication,
                                  Model model) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserDTO user = userPrincipal.getUser();
        UserDTO recentUser = userService.getUserByEmail(user.getEmail());
        model.addAttribute("user", recentUser);
        return USER_PROFILE_PAGE;
    }

    @PostMapping("/public/user/profile")
    public String updateProfile(@ModelAttribute UserDTO user,
                                BindingResult bindingResult,
                                Model model) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            logger.info("User not valid ,errors {}", Arrays.toString(bindingResult.getAllErrors().toArray()));
            return USER_PROFILE_PAGE;
        }
        userService.updateUserInfo(user);
        return REDIRECT_USER_PROFILE;
    }
}
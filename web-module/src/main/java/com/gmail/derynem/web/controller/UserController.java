package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.RoleService;
import com.gmail.derynem.service.UserPasswordService;
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

import static com.gmail.derynem.web.constants.PageNamesConstant.ADD_USER_PAGE;
import static com.gmail.derynem.web.constants.PageNamesConstant.USERS_PAGE;
import static com.gmail.derynem.web.constants.PageNamesConstant.USER_PROFILE_PAGE;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_LIMIT;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_PAGE;
import static com.gmail.derynem.web.constants.PageParamConstant.MESSAGE_PARAM;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_PRIVATE_USERS;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_USER_PROFILE;

@Controller
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;
    private final UserPasswordService userPasswordService;

    public UserController(UserService userService,
                          RoleService roleService,
                          UserValidator userValidator,
                          UserPasswordService userPasswordService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
        this.userPasswordService = userPasswordService;
    }

    @GetMapping("/private/users")
    public String showUsers(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
                            @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
                            UpdateRoleDTO roleUpdate) {
        PageDTO<UserDTO> usersPage = userService.getUsersPageInfo(page, limit);
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
            return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "update user role fail");
        }
        try {
            userService.updateUserRole(updateRoleDTO);
            return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "role updated");
        } catch (UserServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "update user role fail");
        }
    }

    @PostMapping("/private/users/delete")
    public String deleteUsers(@RequestParam(value = "ids", required = false) Long[] ids) {
        if (ids == null) {
            logger.info("no selected users for delete");
            return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "user delete fail");
        } else {
            try {
                userService.deleteUsers(ids);
                return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "Users deleted");
            } catch (UserServiceException e) {
                logger.error(e.getMessage(), e);
                return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "user delete fail");
            }
        }
    }

    @GetMapping("/private/user/new")
    public String showAddUserPage(AddUserDTO user,
                                  Model model) {
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
            return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "user added");
        } catch (UserServiceException e) {
            logger.error(e.getMessage());
            return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "user add fail");
        }
    }

    @PostMapping("/private/user/{id}/password")
    public String changePassword(@PathVariable("id") Long id) {
        if (id == null) {
            logger.info(" user id is null");
            return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "fail");
        }
        try {
            userPasswordService.changePassword(id);
            return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "password changed");
        } catch (UserServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_PRIVATE_USERS + String.format(MESSAGE_PARAM, "password change fail");
        }
    }

    @GetMapping("/private/profile")
    public String showUserProfile(Authentication authentication,
                                  Model model) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserDTO user = userPrincipal.getUser();
        UserDTO recentUser = userService.getUserByEmail(user.getEmail(), false);
        model.addAttribute("user", recentUser);
        return USER_PROFILE_PAGE;
    }

    @PostMapping("/private/profile")
    public String updateProfile(@ModelAttribute(value = "user") UserDTO user,
                                BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.info("User not valid ,errors {}", Arrays.toString(bindingResult.getAllErrors().toArray()));
            return USER_PROFILE_PAGE;
        }
        userService.updateUserInfo(user);
        return REDIRECT_USER_PROFILE + String.format(MESSAGE_PARAM, "profile updated");
    }
}
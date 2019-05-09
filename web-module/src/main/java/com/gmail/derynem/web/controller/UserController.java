package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.RoleService;
import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.role.RoleDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

import static com.gmail.derynem.web.constants.PageNamesConstant.ADD_USER_PAGE;
import static com.gmail.derynem.web.constants.PageNamesConstant.USERS_PAGE;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_PRIVATE_USERS;

@Controller
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RoleService roleService;
    private final PageService pageService;

    public UserController(UserService userService, RoleService roleService, PageService pageService) {
        this.userService = userService;
        this.roleService = roleService;
        this.pageService = pageService;
    }

    @GetMapping("/private/users")
    public String showUsers(Model model, @RequestParam(value = "page", required = false) Integer page,
                            UpdateRoleDTO updateObj) {
        PageDTO pages = userService.getPages();
        Integer validPage = pageService.getValidPage(page, pages.getCount().size());
        model.addAttribute("pages", pages);
        List<RoleDTO> roles = roleService.getRoles();
        List<UserDTO> users = userService.getUsers(validPage);
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("updateObj", updateObj);
        return USERS_PAGE;
    }

    @PostMapping("/private/users/role")
    public String changeStatus(@ModelAttribute(value = "updateObj")
                               @Valid UpdateRoleDTO updateRoleDTO,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return REDIRECT_PRIVATE_USERS;
        }
        userService.updateUserRole(updateRoleDTO);
        return REDIRECT_PRIVATE_USERS;
    }

    @PostMapping("/private/users")
    public String deleteUsers(@RequestParam(value = "ids", required = false) int[] ids) {
        if (ids == null) {
            logger.info("no selected users for delete");
            return REDIRECT_PRIVATE_USERS;
        } else {
            userService.deleteUsers(ids);
            return REDIRECT_PRIVATE_USERS;
        }
    }

    @GetMapping("/private/user")
    public String showAddUserPage(AddUserDTO user, Model model) {
        List<RoleDTO> roles = roleService.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return ADD_USER_PAGE;
    }

    @PostMapping("/private/user")
    public String addUser(@ModelAttribute(value = "user")
                          @Valid AddUserDTO user,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ADD_USER_PAGE;
        }
        userService.addUser(user);
        return REDIRECT_PRIVATE_USERS;
    }

    @PostMapping("/private/user/{id}/password")
    public String changePassword(@PathVariable("id") Long id) {
        if (id == null) {
            logger.info(" user id is null");
            return REDIRECT_PRIVATE_USERS;
        }
        userService.changePassword(id);
        return REDIRECT_PRIVATE_USERS;
    }
}

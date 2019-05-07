package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.RoleService;
import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.model.AddUserDTO;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.UpdateRoleDTO;
import com.gmail.derynem.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

import static com.gmail.derynem.web.constants.PageNames.ADD_USER_PAGE;
import static com.gmail.derynem.web.constants.PageNames.USERS_PAGE;

@Controller
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/private/users")
    public String showUsers(Model model, @RequestParam(value = "page", required = false) Integer page,
                            UpdateRoleDTO updateObj) {
        PageDTO pages = userService.getPages();
        if (page == null) {
            page = 1;
        } else if (page > pages.getCount().size()) {
            page = pages.getCount().size();
        }
        model.addAttribute("pages", pages);
        List<String> roles = roleService.getListOfRoleNames();
        List<UserDTO> users = userService.getUsers(page);
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
            return USERS_PAGE;
        }
        userService.updateUserRole(updateRoleDTO.getRole(), updateRoleDTO.getId());
        return "redirect:/private/users";
    }

    @PostMapping("/private/users")
    public String deleteUsers(@RequestParam(value = "ids", required = false) int[] ids) {
        if (ids == null) {
            logger.info("no selected users for delete");
            return "redirect:/private/users";
        } else {
            userService.deleteUsers(ids);
            return "redirect:/private/users";
        }
    }

    @GetMapping("/private/user")
    public String showAddUserPage(AddUserDTO user, Model model) {
        List<String> roles = roleService.getListOfRoleNames();
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
        return "redirect:/private/users";
    }
}

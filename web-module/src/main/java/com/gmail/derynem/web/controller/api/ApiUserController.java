package com.gmail.derynem.web.controller.api;

import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.model.user.AddUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping("/api/v1.0")
public class ApiUserController {
    private final static Logger logger = LoggerFactory.getLogger(ApiUserController.class);
    private final UserService userService;

    public ApiUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity saveUser(@RequestBody @Valid AddUserDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info(" user not valid cause {}", Arrays.toString(bindingResult.getAllErrors().toArray()));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            try {
                userService.saveUser(user);
                return new ResponseEntity(HttpStatus.CREATED);
            } catch (UserServiceException e) {
                logger.error(e.getMessage(), e);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
    }
}
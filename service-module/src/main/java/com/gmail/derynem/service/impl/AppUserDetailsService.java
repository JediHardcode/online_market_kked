package com.gmail.derynem.service.impl;

import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.model.UserDTO;
import com.gmail.derynem.service.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final static Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);
    private final UserService userService;

    public AppUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDTO foundUser = userService.getUserByEmail(s);
        if (foundUser == null) {
            logger.info("Not found user in database");
            throw new UsernameNotFoundException("not found user with by current username");
        }
        return new UserPrincipal(foundUser);
    }
}

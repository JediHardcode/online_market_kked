package com.gmail.derynem.service;

import com.gmail.derynem.service.exception.UserServiceException;

public interface UserPasswordService {
    void changePassword(Long id) throws UserServiceException;
}
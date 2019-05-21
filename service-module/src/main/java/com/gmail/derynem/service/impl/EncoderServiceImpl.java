package com.gmail.derynem.service.impl;

import com.gmail.derynem.service.EncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncoderServiceImpl implements EncoderService {
    private final PasswordEncoder encoder;

    @Autowired
    public EncoderServiceImpl(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encodePassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean comparePasswords(String enteredPassword, String oldPassword) {
        return encoder.matches(enteredPassword, oldPassword);
    }
}
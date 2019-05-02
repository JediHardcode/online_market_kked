package com.gmail.derynem.service.impl;

import com.gmail.derynem.service.EncoderService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncoderServiceImpl implements EncoderService {
    private final PasswordEncoder encoder;

    public EncoderServiceImpl(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encorePassword(String password) {
        return encoder.encode(password);
    }
}

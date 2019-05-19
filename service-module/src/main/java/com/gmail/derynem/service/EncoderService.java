package com.gmail.derynem.service;

public interface EncoderService {
    String encodePassword(String password);

    boolean comparePasswords(String enteredPassword, String oldPassword);
}
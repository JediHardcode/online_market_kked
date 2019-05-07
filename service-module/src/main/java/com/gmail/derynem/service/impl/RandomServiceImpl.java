package com.gmail.derynem.service.impl;

import com.gmail.derynem.service.RandomService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomServiceImpl implements RandomService {
    @Override
    public String generatePassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        return upperCaseLetters +
                lowerCaseLetters +
                numbers +
                specialChar +
                totalChars;
    }
}

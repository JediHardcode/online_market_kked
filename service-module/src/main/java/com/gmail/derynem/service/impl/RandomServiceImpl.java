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
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        return upperCaseLetters +
                lowerCaseLetters +
                numbers +
                totalChars;
    }

    @Override
    public String generateUniqueNum() {
        String uniquePart = String.valueOf(System.currentTimeMillis());
        String secondPart = RandomStringUtils.randomNumeric(2);
        return uniquePart + secondPart;
    }
}
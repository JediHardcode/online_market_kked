package com.gmail.derynem.service.validation.constant;

public class ValidationConstant {
    public static final String EMAIL_PATTERN = "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$";
    public static final int EMAIL_LENGTH = 50;
    public static final int NAME_LENGTH = 20;
    public static final String ONLY_ENG_LETTER_PATTERN = "[a-zA-Z]+";
    public static final int MIDDLE_NAME_LENGTH = 40;
    public static final int SURNAME_LENGTH = 40;

    private ValidationConstant() {
    }
}

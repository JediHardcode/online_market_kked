package com.gmail.derynem.service.validation.constant;

public class ValidationConstant {
    public static final String EMAIL_PATTERN = "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$";
    public static final int EMAIL_LENGTH = 50;
    public static final int NAME_LENGTH = 20;
    public static final String ONLY_ENG_LETTER_PATTERN = "[a-zA-Z]+";
    public static final String ONLY_ENG_WITH_SPACE = "^[a-zA-Z\\s]*$";
    public static final int MIDDLE_NAME_LENGTH = 40;
    public static final int SURNAME_LENGTH = 40;
    public static final int ARTICLE_CONTENT_LENGTH = 1000;
    public static final String ONLY_DIGITS = "^\\d+$";
    public static final int PASSWORD_LENGTH = 20;
    public static final int ADDRESS_LENGTH = 80;
    public static final int TELEPHONE_LENGTH = 12;
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,}$";
    public static final int COMMENT_LENGTH = 200;

    private ValidationConstant() {
    }
}
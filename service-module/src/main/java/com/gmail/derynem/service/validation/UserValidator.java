package com.gmail.derynem.service.validation;

import com.gmail.derynem.service.EncoderService;
import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.model.user.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.gmail.derynem.service.validation.constant.ValidationConstant.ADDRESS_LENGTH;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.NAME_LENGTH;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.ONLY_DIGITS;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.ONLY_ENG_LETTER_PATTERN;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.ONLY_ENG_WITH_SPACE;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.PASSWORD_LENGTH;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.PASSWORD_PATTERN;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.SURNAME_LENGTH;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.TELEPHONE_LENGTH;

@Component
public class UserValidator implements Validator {
    private final UserService userService;
    private final EncoderService encoderService;

    public UserValidator(UserService userService, EncoderService encoderService) {
        this.userService = userService;
        this.encoderService = encoderService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        UserDTO user = (UserDTO) object;
        validateName(user, errors);
        validateSurname(user, errors);
        validatePassword(user, errors);
        validateAddress(user, errors);
        validatePhone(user, errors);
    }

    private void validatePassword(UserDTO user, Errors errors) {
        UserDTO userDTO = userService.getUserByEmail(user.getEmail(), false);
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errors.rejectValue("password", "user.password.empty", "password is empty");
            return;
        } else if (user.getPassword().length() > PASSWORD_LENGTH || !user.getPassword().matches(PASSWORD_PATTERN)) {
            errors.rejectValue("password", "user.password.not.valid", " password not valid, must contain at least 1 letter and 1 digit ");
            return;
        }
        if (userDTO != null) {
            String oldPassword = userDTO.getPassword();
            if (encoderService.comparePasswords(user.getPassword(), oldPassword)) {
                errors.rejectValue("password", "user.password.matched", "password should not match with the old password");
            }
        }
    }

    private void validatePhone(UserDTO user, Errors errors) {
        if (user.getProfile().getTelephone() == null || user.getProfile().getTelephone().isEmpty()) {
            errors.rejectValue("profile.telephone", "profile.telephone.empty", " phone is empty");
        } else if (user.getProfile().getTelephone().length() > TELEPHONE_LENGTH
                || !user.getProfile().getTelephone().matches(ONLY_DIGITS)) {
            errors.rejectValue("profile.telephone", "profile.telephone.not.valid", "phone not valid");
        }
    }

    private void validateAddress(UserDTO user, Errors errors) {
        if (user.getProfile().getAddress() == null || user.getProfile().getAddress().isEmpty()) {
            errors.rejectValue("profile.address", "profile.address.empty", "address is empty");
        } else if (user.getProfile().getAddress().length() > ADDRESS_LENGTH
                || !user.getProfile().getAddress().matches(ONLY_ENG_WITH_SPACE)) {
            errors.rejectValue("profile.address", "profile.address.not.valid", " address not valid");
        }
    }

    private void validateSurname(UserDTO user, Errors errors) {
        if (user.getSurName() == null || user.getSurName().isEmpty()) {
            errors.rejectValue("surName", "user.surname.empty", " surname is empty");
        } else if (user.getSurName().length() > SURNAME_LENGTH || !user.getSurName().matches(ONLY_ENG_LETTER_PATTERN)) {
            errors.rejectValue("surName", "user.surname.not.valid", " surname not valid");
        }
    }

    private void validateName(UserDTO user, Errors errors) {
        if (user.getName() == null || user.getName().isEmpty()) {
            errors.rejectValue("name", "user.name.empty", " name is empty");
        } else if (user.getName().length() > NAME_LENGTH || !user.getName().matches(ONLY_ENG_LETTER_PATTERN)) {
            errors.rejectValue("name", "user.not.valid", "name not valid");
        }
    }
}
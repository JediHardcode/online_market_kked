package com.gmail.derynem.service.converter.user.impl;

import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.converter.user.UserConverterAssembler;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserCommonDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UserConverterAssemblerImpl implements UserConverterAssembler {
    private final Converter<UserDTO, User> userConverter;
    private final Converter<UserCommonDTO, User> userCommonConverter;
    private final Converter<AddUserDTO, User> addUserConverter;

    public UserConverterAssemblerImpl(@Qualifier("userMainConverter") Converter<UserDTO, User> userConverter,
                                      @Qualifier("userCommonConverter") Converter<UserCommonDTO, User> userCommonConverter,
                                      @Qualifier("addUserConverter") Converter<AddUserDTO, User> addUserConverter) {
        this.userConverter = userConverter;
        this.userCommonConverter = userCommonConverter;
        this.addUserConverter = addUserConverter;
    }

    @Override
    public Converter<UserDTO, User> getUserConverter() {
        return userConverter;
    }

    @Override
    public Converter<UserCommonDTO, User> getUserCommonConverter() {
        return userCommonConverter;
    }

    @Override
    public Converter<AddUserDTO, User> getAddUserConverter() {
        return addUserConverter;
    }
}
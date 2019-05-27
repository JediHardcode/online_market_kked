package com.gmail.derynem.service.converter.user;

import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserCommonDTO;
import com.gmail.derynem.service.model.user.UserDTO;

public interface UserConverterAssembler {
    Converter<UserDTO, User> getUserConverter();

    Converter<UserCommonDTO, User> getUserCommonConverter();

    Converter<AddUserDTO, User> getAddUserConverter();
}
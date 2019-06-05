package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.UserPasswordService;
import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.EncoderService;
import com.gmail.derynem.service.MailService;
import com.gmail.derynem.service.RandomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPasswordServiceImpl implements UserPasswordService {
    private final static Logger logger = LoggerFactory.getLogger(UserPasswordServiceImpl.class);
    private final UserRepository userRepository;
    private final MailService mailService;
    private final RandomService randomService;
    private final EncoderService encoderService;

    public UserPasswordServiceImpl(UserRepository userRepository,
                                   MailService mailService,
                                   RandomService randomService,
                                   EncoderService encoderService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.randomService = randomService;
        this.encoderService = encoderService;
    }

    @Override
    @Transactional
    public void changePassword(Long id) throws UserServiceException {
        User user = userRepository.getById(id);
        if (user != null) {
            String newPassword = randomService.generatePassword();
            mailService.sendMessage(user.getEmail(), newPassword);
            logger.info("user with email {} have new password {}", user.getEmail(), newPassword);
            user.setPassword(encoderService.encodePassword(newPassword));
            userRepository.merge(user);
        } else {
            throw new UserServiceException("user with id" + id + " not found");
        }
    }
}
package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.EncoderService;
import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.RandomService;
import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.converter.UserConverter;
import com.gmail.derynem.service.exception.UserServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.role.UpdateRoleDTO;
import com.gmail.derynem.service.model.user.AddUserDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.derynem.service.constants.PageConstant.OFFSET_LIMIT;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PageService pageService;
    private final RandomService randomService;
    private final EncoderService encoderService;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter,
                           PageService pageService, RandomService randomService,
                           EncoderService encoderService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.pageService = pageService;
        this.randomService = randomService;
        this.encoderService = encoderService;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDTO getUserByEmail(String email) {
        User foundUser = userRepository.getByEmail(email);
        if (foundUser == null) {
            logger.info(" no user with this email {} in database", email);
            return null;
        }
        UserDTO userDTO = userConverter.toDTO(foundUser);
        logger.info("user found with current email{} , user name is {} ", email, userDTO.getName());
        return userDTO;
    }

    @Override
    @Transactional
    public PageDTO<UserDTO> getUsersPageInfo(Integer page) {
        int countOfUsers = userRepository.getCountOfEntities();
        int countOfPages = pageService.getPages(countOfUsers, OFFSET_LIMIT);
        int offset = pageService.getOffset(page, countOfPages, OFFSET_LIMIT);
        List<User> users = userRepository.findAll(offset, OFFSET_LIMIT);
        List<UserDTO> userDTOList = users.stream()
                .map(userConverter::toDTO)
                .collect(Collectors.toList());
        PageDTO<UserDTO> usersPageInfo = new PageDTO<>();
        usersPageInfo.setEntities(userDTOList);
        usersPageInfo.setCountOfPages(countOfPages);
        logger.info(" users found, count of users {}, count of pages {}", usersPageInfo.getEntities().size(), countOfPages);
        return usersPageInfo;
    }

    @Override
    @Transactional
    public void updateUserRole(UpdateRoleDTO updateRoleDTO) throws UserServiceException {
        User user = userRepository.getById(updateRoleDTO.getId());
        if (user == null) {
            throw new UserServiceException(" user with id " + updateRoleDTO.getId() + " not found");
        }
        Role role = roleRepository.getById(updateRoleDTO.getRoleId());
        user.setRole(role);
        userRepository.merge(user);
        logger.info("user updated, new role  is {}", role.getName());
    }

    @Override
    @Transactional
    public void deleteUsers(Long[] ids) throws UserServiceException {
        for (int i = 0; i < ids.length; i++) {
            User user = userRepository.getById(ids[i]);
            if (user == null) {
                throw new UserServiceException("user with id " + ids[i] + "n ot found in database");
            }
            userRepository.remove(user);
            logger.info("user with name {} deleted ", user.getName());
        }
    }

    @Override
    @Transactional
    public void saveUser(AddUserDTO userDTO) throws UserServiceException {
        User matchedUser = userRepository.getByEmail(userDTO.getEmail());
        if (matchedUser == null) {
            User user = userConverter.fromAddUserToUser(userDTO);
            user.setPassword(encoderService.encodePassword(randomService.generatePassword()));
            userRepository.persist(user);
            logger.info(" user added to database , user id {}", user.getId());
        } else {
            logger.info(" user with email {} already exist in database", userDTO.getEmail());
            throw new UserServiceException(" user already exist in database ");
        }
    }

    @Override
    @Transactional
    public void changePassword(Long id) throws UserServiceException {
        User user = userRepository.getById(id);
        if (user != null) {
            String newPassword = randomService.generatePassword();
            logger.info("user {} have new password {}", user.getName(), newPassword);
            user.setPassword(encoderService.encodePassword(newPassword));
            userRepository.merge(user);
        } else {
            throw new UserServiceException("user with id" + id + " not found");
        }
    }

    @Override
    @Transactional
    public void updateUserInfo(UserDTO userDTO) {
        User user = userRepository.getById(userDTO.getId());
        if (user != null) {
            user.getProfile().setTelephone(userDTO.getProfile().getTelephone());
            user.getProfile().setAddress(userDTO.getProfile().getAddress());
            user.setName(userDTO.getName());
            user.setSurName(userDTO.getSurName());
            user.setPassword(encoderService.encodePassword(userDTO.getPassword()));
            logger.info("user with  id {} updated, new name:{}, new surname: {}, new address: {},new phone:{}",
                    user.getId(), user.getName(), user.getSurName(), user.getProfile().getAddress(), user.getProfile().getTelephone());
            userRepository.merge(user);
        } else {
            logger.info(" not found user with this id {}", userDTO.getId());
        }
    }

    @Override
    @Transactional
    public UserDTO getById(Long id) {
        User user = userRepository.getById(id);
        if (user == null) {
            logger.info("user with id {} doesnt exist in database", id);
            return null;
        }
        UserDTO userDTO = userConverter.toDTO(user);
        logger.info(" user found , user name {}", userDTO.getName());
        return userDTO;
    }
}
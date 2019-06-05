package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.Role;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.EncoderService;
import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.RandomService;
import com.gmail.derynem.service.UserService;
import com.gmail.derynem.service.converter.user.UserConverterAssembler;
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

@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserConverterAssembler userConverterAssembler;
    private final PageService pageService;
    private final RandomService randomService;
    private final EncoderService encoderService;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           UserConverterAssembler userConverterAssembler,
                           PageService pageService,
                           RandomService randomService,
                           EncoderService encoderService,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userConverterAssembler = userConverterAssembler;
        this.pageService = pageService;
        this.randomService = randomService;
        this.encoderService = encoderService;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDTO getUserByEmail(String email, Boolean isDeleted) {
        User foundUser = userRepository.getByEmail(email, isDeleted);
        if (foundUser == null) {
            logger.info(" no user with this email {} in database", email);
            return null;
        }
        UserDTO userDTO = userConverterAssembler.getUserConverter().toDTO(foundUser);
        logger.info("user found with current email{} , user name is {} ", email, userDTO.getName());
        return userDTO;
    }

    @Override
    @Transactional
    public PageDTO<UserDTO> getUsersPageInfo(Integer page, Integer limit) {
        int validLimit = pageService.validateLimit(limit);
        int countOfUsers = userRepository.getCountOfEntities();
        int countOfPages = pageService.getPages(countOfUsers, validLimit);
        int offset = pageService.getOffset(page, countOfPages, validLimit);
        List<User> users = userRepository.findAll(offset, validLimit);
        List<UserDTO> userDTOList = users.stream()
                .map(user -> userConverterAssembler.getUserConverter().toDTO(user))
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
        if (user.isInviolable()) {
            logger.info("main administrator cannot be deleted");
            throw new UserServiceException("main administrator cannot be deleted");
        }
        Role role = roleRepository.getById(updateRoleDTO.getRoleId());
        user.setRole(role);
        userRepository.merge(user);
        logger.info("user updated, new role  is {}", role.getName());
    }

    @Override
    @Transactional
    public void deleteUsers(Long[] ids) throws UserServiceException {
        for (Long id : ids) {
            User user = userRepository.getById(id);
            if (user == null) {
                throw new UserServiceException("user with id " + id + "not found in database");
            }
            userRepository.remove(user);
            logger.info("user with name {} deleted ", user.getName());
        }
    }

    @Override
    @Transactional
    public void saveUser(AddUserDTO userDTO) throws UserServiceException {
        User matchedUser = userRepository.getByEmail(userDTO.getEmail(), null);
        if (matchedUser == null) {
            User user = userConverterAssembler.getAddUserConverter().toEntity(userDTO);
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
}
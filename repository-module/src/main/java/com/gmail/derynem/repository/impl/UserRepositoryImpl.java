package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public User getByEmail(String email) {
        String queryString = "SELECT e FROM " + entityClass.getName() + " e" + " WHERE e.email = :email";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("email", email);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException e) {
            logger.info(" user with email {} not found in database", email);
            return null;
        }
    }
}
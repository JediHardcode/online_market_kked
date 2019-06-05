package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public User getByEmail(String email) {
        String queryString = "select e from " + entityClass.getName() + " e" + " where e.email = :email and e.deleted = false";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("email", email);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            logger.info(" user with email {} not found in database", email);
            return null;
        }
    }

    @Override
    public int getCountOfEntities() {
        String query = "select count(*) from " + entityClass.getName() + " e where e.deleted = false";
        Query q = entityManager.createQuery(query);
        try {
            return ((Number) q.getSingleResult()).intValue();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            logger.info("no available users ");
            return 0;
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<User> findAll(int offset, int limit) {
        String query = "from " + entityClass.getName() + " e where e.deleted = false order by e.email asc";
        Query q = entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(limit);
        try {
            return q.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
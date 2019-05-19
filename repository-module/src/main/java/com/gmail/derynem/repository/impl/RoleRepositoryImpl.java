package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.RoleRepository;
import com.gmail.derynem.repository.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {
    private final static Logger logger = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Role> getRoles() {
        String query = "from " + entityClass.getName() + " e";
        Query q = entityManager.createQuery(query);
        try {
            return q.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            logger.info("no available roles");
            return Collections.emptyList();
        }
    }
}
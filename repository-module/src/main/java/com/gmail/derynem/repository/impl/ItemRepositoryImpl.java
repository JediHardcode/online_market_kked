package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.ItemRepository;
import com.gmail.derynem.repository.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Repository
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {
    private final static Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Item> findAll(int offset, int limit) {
        String query = "from " + entityClass.getName() + " e where e.isDeleted = false order by e.name asc";
        Query q = entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(limit);
        try {
            return q.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            logger.info(" no available items");
            return Collections.emptyList();
        }
    }

    @Override
    public int getCountOfEntities() {
        String query = "select count(*) from " + entityClass.getName() + " e where e.isDeleted= false";
        Query q = entityManager.createQuery(query);
        try {
            return ((Number) q.getSingleResult()).intValue();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            logger.info("no available entities");
            return 0;
        }
    }
}
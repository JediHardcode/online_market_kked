package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.OrderRepository;
import com.gmail.derynem.repository.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {
    private final static Logger logger = LoggerFactory.getLogger(OrderRepositoryImpl.class);

    @Override
    public int getCountOfOrders(Long id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select count(*) from ")
                .append(entityClass.getName())
                .append(" e");
        if (id != null) {
            stringBuilder.append(" where e.user.id= ").append(id);
        }
        Query q = entityManager.createQuery(stringBuilder.toString());
        try {
            return ((Number) q.getSingleResult()).intValue();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            logger.info("no available orders ");
            return 0;
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Order> getOrders(int offset, Integer limit, Long id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("from ").append(entityClass.getName()).append(" e");
        if (id != null) {
            stringBuilder.append(" where e.user.id = ").append(id);
        }
        stringBuilder.append(" order by e.created desc");
        Query q = entityManager.createQuery(stringBuilder.toString())
                .setFirstResult(offset)
                .setMaxResults(limit);
        try {
            return q.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            logger.info(" no available orders");
            return Collections.emptyList();
        }
    }
}
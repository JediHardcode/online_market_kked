package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.ReviewRepository;
import com.gmail.derynem.repository.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {
    private final static Logger logger = LoggerFactory.getLogger(ReviewRepositoryImpl.class);

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Review> getReviews(int offset, int limit, Boolean isHidden) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("from ").append(entityClass.getName()).append(" e");
        if (isHidden != null) {
            stringBuilder.append(" where e.hidden = ").append(isHidden);
        }
        Query q = entityManager.createQuery(stringBuilder.toString())
                .setFirstResult(offset)
                .setMaxResults(limit);
        try {
            return q.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            logger.info(" no available reviews");
            return Collections.emptyList();
        }
    }

    @Override
    public int getCountOfReviews(Boolean isHidden) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select count(*) from ")
                .append(entityClass.getName())
                .append(" e");
        if (isHidden != null) {
            stringBuilder.append(" where e.hidden = ").append(isHidden);
        }
        Query q = entityManager.createQuery(stringBuilder.toString());
        try {
            return ((Number) q.getSingleResult()).intValue();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            logger.info("no available reviews ");
            return 0;
        }
    }

    @Override
    public int changeIsHiddenStatus(boolean isHidden, List<Long> ids) {
        String queryString = "update " + entityClass.getName() + " e" +
                " set e.hidden = :condition where e.id in (:ids) and e.hidden = :currentStatus";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("ids", ids);
        query.setParameter("condition", isHidden);
        query.setParameter("currentStatus", !isHidden);
        int result = query.executeUpdate();
        logger.info(" review changes successfully , count of changed status reviews is {}", result);
        return result;
    }
}
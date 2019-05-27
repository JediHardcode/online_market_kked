package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.ArticleRepository;
import com.gmail.derynem.repository.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {
    private final static Logger logger = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Article> findAll(int offset, int limit) {
        String query = "from " + entityClass.getName() + " e" + " order by e.created desc";
        Query q = entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(limit);
        try {
            return q.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            logger.info(" no available articles");
            return Collections.emptyList();
        }
    }
}
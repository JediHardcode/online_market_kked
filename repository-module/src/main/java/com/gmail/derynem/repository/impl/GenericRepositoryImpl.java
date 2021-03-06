package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.GenericRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

public class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {
    private final static Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);
    @PersistenceContext
    protected EntityManager entityManager;
    protected Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) superClass.getActualTypeArguments()[1];
    }

    @Override
    public void persist(T entity) {
        entityManager.persist(entity);
        logger.info("Object {} added to database ", entity.getClass().getName());
    }

    @Override
    public void merge(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public T getById(I id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<T> findAll(int offset, int limit) {
        String query = "from " + entityClass.getName() + " e";
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

    @Override
    public int getCountOfEntities() {
        String query = "select count(*) from " + entityClass.getName() + " e";
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
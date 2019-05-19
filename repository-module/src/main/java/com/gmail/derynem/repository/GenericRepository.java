package com.gmail.derynem.repository;

import java.sql.Connection;
import java.util.List;

public interface GenericRepository<I, T> {
    Connection getConnection();

    void persist(T entity);

    void merge(T entity);

    void remove(T entity);

    T getById(I id);

    @SuppressWarnings({"unchecked", "rawtypes"})
    List<T> findAll(int offset, int limit);

    int getCountOfEntities();
}
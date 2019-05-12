package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.GenericRepository;
import com.gmail.derynem.repository.exception.GenericRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GenericRepositoryImpl implements GenericRepository {
    private final static Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);
    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new GenericRepositoryException(e.getMessage(), e);
        }
    }
}
package com.gmail.derynem.repository.connection.impl;

import com.gmail.derynem.repository.connection.GenericRepository;
import com.gmail.derynem.repository.exception.GenericRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GenericRepositoryImpl implements GenericRepository {
    @Autowired
    private DataSource dataSource;
    private final static Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);


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

package com.gmail.derynem.repository.connection.impl;

import com.gmail.derynem.repository.exception.ConnectionServiceException;
import com.gmail.derynem.repository.exception.ItemRepositoryException;
import com.gmail.derynem.repository.connection.ConnectionService;
import com.gmail.derynem.service.property.DataBaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class ConnectionServiceImpl implements ConnectionService {
    private final DataBaseProperties dataBaseProperties;
    private static final Logger logger = LoggerFactory.getLogger(ConnectionServiceImpl.class);

    @Autowired
    public ConnectionServiceImpl(DataBaseProperties dataBaseProperties) {
        try {
            Class.forName(dataBaseProperties.getDatabaseDriverName());
            logger.info("Database driver upload");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionServiceException(e.getMessage(), e);
        }
        this.dataBaseProperties = dataBaseProperties;
    }

    @Override
    public Connection getConnection() {
        try {
            Properties properties = new Properties();
            properties.setProperty("user", dataBaseProperties.getDatabaseUser());
            properties.setProperty("password", dataBaseProperties.getDatabasePassword());
            logger.info(" connection is created and ready for work");
            return DriverManager.getConnection(dataBaseProperties.getDatabaseUrl(), properties);
        } catch (SQLException e) {
            logger.error("Error with database getting connection||" + e.getMessage(), e);
            throw new ConnectionServiceException(e.getMessage(), e);
        }
    }
}

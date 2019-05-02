package com.gmail.derynem.service.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataBaseProperties {
    @Value("${spring.datasource.password:}")
    private String databasePassword;
    @Value("${spring.datasource.username}")
    private String databaseUser;
    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.driver-class-name}")
    private String databaseDriverName;
    @Value("${spring.config.location}")
    private String initialDataSql;

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseDriverName() {
        return databaseDriverName;
    }

    public String getInitialDataSql() {
        return initialDataSql;
    }
}

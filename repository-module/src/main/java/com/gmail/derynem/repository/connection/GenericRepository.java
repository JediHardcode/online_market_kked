package com.gmail.derynem.repository.connection;

import java.sql.Connection;

public interface GenericRepository {
    Connection getConnection();
}

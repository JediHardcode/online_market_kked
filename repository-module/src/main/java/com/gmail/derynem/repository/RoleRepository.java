package com.gmail.derynem.repository;

import java.sql.Connection;
import java.util.List;

public interface RoleRepository extends GenericRepository {
    List<String> getListOfRoleNames(Connection connection);

    Long getRoleIdByRoleName(Connection connection, String role);
}

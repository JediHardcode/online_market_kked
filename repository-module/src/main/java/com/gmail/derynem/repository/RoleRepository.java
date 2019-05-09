package com.gmail.derynem.repository;

import com.gmail.derynem.repository.model.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleRepository extends GenericRepository {
    List<Role> getRoles(Connection connection);

    Long getRoleIdByRoleName(Connection connection, String role);
}

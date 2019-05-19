package com.gmail.derynem.repository;

import com.gmail.derynem.repository.model.Role;

import java.util.List;

public interface RoleRepository extends GenericRepository<Long, Role> {
    List<Role> getRoles();
}
package com.gmail.derynem.repository;

import com.gmail.derynem.repository.model.User;

public interface UserRepository extends GenericRepository<Long, User> {
    User getByEmail(String email, Boolean isDeleted);
}
package com.gmail.derynem.service;

import com.gmail.derynem.service.exception.CommentServiceException;

public interface CommentService {
    void deleteComment(Long id) throws CommentServiceException;
}
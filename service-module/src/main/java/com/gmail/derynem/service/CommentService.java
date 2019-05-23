package com.gmail.derynem.service;

import com.gmail.derynem.service.exception.ArticleServiceException;

public interface CommentService {
    void deleteComment(Long id) throws ArticleServiceException;
}
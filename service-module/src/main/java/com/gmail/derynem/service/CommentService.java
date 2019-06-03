package com.gmail.derynem.service;

import com.gmail.derynem.service.exception.CommentServiceException;
import com.gmail.derynem.service.model.comment.CommentDTO;

public interface CommentService {
    void deleteComment(Long id) throws CommentServiceException;

    void addComment(CommentDTO commentDTO, Long id);
}
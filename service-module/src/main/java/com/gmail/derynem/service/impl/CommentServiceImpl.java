package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.CommentRepository;
import com.gmail.derynem.repository.model.Comment;
import com.gmail.derynem.service.CommentService;
import com.gmail.derynem.service.exception.ArticleServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {
    private final static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;


    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public void deleteComment(Long id) throws ArticleServiceException {
        Comment comment = commentRepository.getById(id);
        if (comment == null) {
            logger.info("Comment with id {} doesnt exist in database ", id);
            throw new ArticleServiceException("comment doesnt exist in database");
        } else {
            commentRepository.remove(comment);
            logger.info("Comment with id{} successfully removed from database", comment.getId());
        }
    }
}
package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.ArticleRepository;
import com.gmail.derynem.repository.CommentRepository;
import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.Article;
import com.gmail.derynem.repository.model.Comment;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.CommentService;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.exception.CommentServiceException;
import com.gmail.derynem.service.model.comment.CommentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {
    private final static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final Converter<CommentDTO, Comment> converter;
    private final UserRepository userRepository;


    public CommentServiceImpl(CommentRepository commentRepository,
                              ArticleRepository articleRepository,
                              @Qualifier("commentConverter") Converter<CommentDTO, Comment> converter,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.converter = converter;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void deleteComment(Long id) throws CommentServiceException {
        Comment comment = commentRepository.getById(id);
        if (comment == null) {
            logger.info("Comment with id {} doesnt exist in database ", id);
            throw new CommentServiceException("comment doesnt exist in database");
        } else {
            commentRepository.remove(comment);
            logger.info("Comment with id{} successfully removed from database", comment.getId());
        }
    }

    @Override
    @Transactional
    public void addComment(CommentDTO commentDTO, Long id) {
        Article article = articleRepository.getById(id);
        Comment comment = converter.toEntity(commentDTO);
        User user = userRepository.getById(commentDTO.getUser().getId());
        comment.setUser(user);
        article.getComments().add(comment);
        articleRepository.merge(article);
    }
}
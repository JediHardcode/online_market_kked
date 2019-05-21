package com.gmail.derynem.repository.impl;

import com.gmail.derynem.repository.CommentRepository;
import com.gmail.derynem.repository.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {
}
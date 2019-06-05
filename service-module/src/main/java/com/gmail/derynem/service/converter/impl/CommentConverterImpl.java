package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Comment;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.converter.user.UserConverterAssembler;
import com.gmail.derynem.service.model.comment.CommentDTO;
import org.springframework.stereotype.Component;

@Component("commentConverter")
public class CommentConverterImpl implements Converter<CommentDTO, Comment> {
    private final UserConverterAssembler userConverterAssembler;

    public CommentConverterImpl(UserConverterAssembler userConverterAssembler) {
        this.userConverterAssembler = userConverterAssembler;
    }

    @Override
    public CommentDTO toDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreated(comment.getCreated());
        commentDTO.setId(comment.getId());
        commentDTO.setUser(userConverterAssembler.getUserCommonConverter().toDTO(comment.getUser()));
        commentDTO.setDeleted(comment.isDeleted());
        return commentDTO;
    }

    @Override
    public Comment toEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setCreated(commentDTO.getCreated());
        comment.setDeleted(commentDTO.isDeleted());
        return comment;
    }
}
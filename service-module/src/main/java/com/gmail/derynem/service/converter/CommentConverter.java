package com.gmail.derynem.service.converter;

import com.gmail.derynem.repository.model.Comment;
import com.gmail.derynem.service.model.comment.CommentDTO;

import java.util.List;

public interface CommentConverter {
    List<CommentDTO> toDTOList(List<Comment> comments);

    CommentDTO toDTO(Comment comment);

    Comment toEntity(CommentDTO commentDTO);

    List<Comment> toEntityList(List<CommentDTO> commentDTOS);
}
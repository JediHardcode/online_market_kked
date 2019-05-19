package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Comment;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.CommentConverter;
import com.gmail.derynem.service.converter.UserConverter;
import com.gmail.derynem.service.model.comment.CommentDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConverterImpl implements CommentConverter {
    private final UserConverter userConverter;

    public CommentConverterImpl(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public List<CommentDTO> toDTOList(List<Comment> comments) {
        return comments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO toDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreated(comment.getCreated());
        commentDTO.setId(comment.getId());
        commentDTO.setUser(userConverter.toDTO(comment.getUser()));
        commentDTO.setDeleted(comment.isDeleted());
        return commentDTO;
    }

    @Override
    public Comment toEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setCreated(commentDTO.getCreated());
        comment.setDeleted(commentDTO.isDeleted());
        User user = new User();
        user.setId(commentDTO.getUser().getId());
        comment.setUser(user);
        return comment;
    }

    @Override
    public List<Comment> toEntityList(List<CommentDTO> commentDTOS) {
        return commentDTOS.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

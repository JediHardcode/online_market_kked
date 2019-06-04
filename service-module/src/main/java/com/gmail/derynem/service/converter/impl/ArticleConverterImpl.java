package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Article;
import com.gmail.derynem.repository.model.Comment;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.converter.user.UserConverterAssembler;
import com.gmail.derynem.service.model.article.ArticleDTO;
import com.gmail.derynem.service.model.comment.CommentDTO;
import com.gmail.derynem.service.model.user.UserCommonDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("articleConverter")
public class ArticleConverterImpl implements Converter<ArticleDTO, Article> {
    private final UserConverterAssembler userConverterAssembler;
    private final Converter<CommentDTO, Comment> commentConverter;

    public ArticleConverterImpl(UserConverterAssembler userConverterAssembler,
                                @Qualifier("commentConverter") Converter<CommentDTO, Comment> commentConverter) {
        this.userConverterAssembler = userConverterAssembler;
        this.commentConverter = commentConverter;
    }

    @Override
    public Article toEntity(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setContent(articleDTO.getContent());
        article.setCreated(articleDTO.getCreated());
        article.setName(articleDTO.getName());
        article.setDeleted(articleDTO.isDeleted());
        User user = new User();
        user.setId(articleDTO.getUser().getId());
        article.setUser(user);
        return article;
    }

    @Override
    public ArticleDTO toDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setContent(article.getContent());
        articleDTO.setCreated(article.getCreated());
        articleDTO.setDeleted(article.isDeleted());
        articleDTO.setId(article.getId());
        articleDTO.setName(article.getName());
        if (article.getComments() != null || !article.getComments().isEmpty()) {
            articleDTO.setComments(article.getComments().stream()
                    .map(commentConverter::toDTO)
                    .collect(Collectors.toList()));
        }
        UserCommonDTO userDTO = userConverterAssembler.getUserCommonConverter().toDTO(article.getUser());
        articleDTO.setUser(userDTO);
        return articleDTO;
    }
}
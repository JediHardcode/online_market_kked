package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Article;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.converter.ArticleConverter;
import com.gmail.derynem.service.converter.CommentConverter;
import com.gmail.derynem.service.converter.UserConverter;
import com.gmail.derynem.service.model.article.ArticleDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class ArticleConverterImpl implements ArticleConverter {
    private final UserConverter userConverter;
    private final CommentConverter commentConverter;

    public ArticleConverterImpl(UserConverter userConverter, CommentConverter commentConverter) {
        this.userConverter = userConverter;
        this.commentConverter = commentConverter;
    }

    @Override
    public Article toArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setContent(articleDTO.getContent());
        article.setCreated(articleDTO.getCreated());
        article.setName(articleDTO.getName());
        article.setDeleted(articleDTO.isDeleted());
        article.setComments(commentConverter.toEntityList(articleDTO.getComments()));
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
        articleDTO.setComments(commentConverter.toDTOList(article.getComments()));
        UserDTO userDTO = userConverter.toDTO(article.getUser());
        articleDTO.setUser(userDTO);
        return articleDTO;
    }
}
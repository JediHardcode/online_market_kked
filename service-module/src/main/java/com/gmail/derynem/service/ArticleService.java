package com.gmail.derynem.service;

import com.gmail.derynem.service.exception.ArticleServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.article.ArticleDTO;

public interface ArticleService {
    void saveArticle(ArticleDTO articleDTO) throws ArticleServiceException;

    ArticleDTO getArticleById(Long id) throws ArticleServiceException;

    PageDTO<ArticleDTO> getArticlePageInfo(Integer page, Integer limit);

    void deleteArticle(Long id) throws ArticleServiceException;

    void updateArticle(ArticleDTO article) throws ArticleServiceException;
}
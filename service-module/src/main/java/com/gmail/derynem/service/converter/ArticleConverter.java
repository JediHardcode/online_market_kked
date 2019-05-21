package com.gmail.derynem.service.converter;

import com.gmail.derynem.repository.model.Article;
import com.gmail.derynem.service.model.article.ArticleDTO;

public interface ArticleConverter {
    Article toArticle(ArticleDTO articleDTO);

    ArticleDTO toDTO(Article article);
}
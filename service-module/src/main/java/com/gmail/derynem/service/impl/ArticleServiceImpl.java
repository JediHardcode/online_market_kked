package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.ArticleRepository;
import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.Article;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.ArticleService;
import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.exception.ArticleServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.article.ArticleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final static int PREVIEW_LENGTH = 200;
    private final ArticleRepository articleRepository;
    private final Converter<ArticleDTO, Article> converter;
    private final PageService pageService;
    private final UserRepository userRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              @Qualifier("articleConverter") Converter<ArticleDTO, Article> converter,
                              PageService pageService,
                              UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.converter = converter;
        this.pageService = pageService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void saveArticle(ArticleDTO articleDTO) throws ArticleServiceException {
        User user = userRepository.getById(articleDTO.getUser().getId());
        if (user != null) {
            Article article = converter.toEntity(articleDTO);
            article.setUser(user);
            articleRepository.persist(article);
            logger.info("article with name {} added to database", article.getName());
        } else {
            throw new ArticleServiceException("author this article not found in database, bad request");
        }
    }

    @Override
    @Transactional
    public ArticleDTO getArticleById(Long id) throws ArticleServiceException {
        Article article = articleRepository.getById(id);
        if (article == null) {
            logger.info("not found article with this id {} in database", id);
            throw new ArticleServiceException("Article with this id " + id + " doesnt exist in database");
        }
        ArticleDTO articleDTO = converter.toDTO(article);
        logger.info(" article with id {} author id {} get from database", articleDTO.getId(), articleDTO.getUser().getId());
        return articleDTO;
    }

    @Override
    @Transactional
    public PageDTO<ArticleDTO> getArticlePageInfo(Integer page, Integer limit) {
        int countOfArticles = articleRepository.getCountOfEntities();
        int countOfPages = pageService.getPages(countOfArticles, limit);
        int offset = pageService.getOffset(page, countOfPages, limit);
        PageDTO<ArticleDTO> articlePageInfo = new PageDTO<>();
        articlePageInfo.setCountOfPages(countOfPages);
        List<Article> articles = articleRepository.findAll(offset, limit);
        List<ArticleDTO> articleDTOS = articles.stream()
                .map(converter::toDTO)
                .peek(this::setPreview)
                .collect(Collectors.toList());
        articlePageInfo.setEntities(articleDTOS);
        logger.info("count of articles {}, count of pages {}",
                articlePageInfo.getEntities().size(), articlePageInfo.getCountOfPages());
        return articlePageInfo;
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) throws ArticleServiceException {
        Article article = articleRepository.getById(id);
        if (article == null) {
            logger.info(" article with  id {} doesnt exist in database", id);
            throw new ArticleServiceException(" article with id " + id + " not found in database");
        }
        articleRepository.remove(article);
        logger.info("article with id {}, name {} , author name {} deleted form database ", article.getId(), article.getName(), article.getUser().getName());
    }

    @Override
    @Transactional
    public void updateArticle(ArticleDTO article) throws ArticleServiceException {
        Article updatedArticle = articleRepository.getById(article.getId());
        if (updatedArticle == null) {
            throw new ArticleServiceException("article with id " + article.getId() + " not found in database");
        }
        updatedArticle.setContent(article.getContent());
        updatedArticle.setName(article.getName());
        updatedArticle.setCreated(String.valueOf(LocalDateTime.now()));
        articleRepository.merge(updatedArticle);
        logger.info("article with id {} was successfully updated, new name : {}, created {}",
                updatedArticle.getId(), updatedArticle.getName(), updatedArticle.getCreated());
    }

    private void setPreview(ArticleDTO articleDTO) {
        if (articleDTO.getContent().length() > PREVIEW_LENGTH) {
            articleDTO.setPreview(articleDTO.getContent().substring(0, PREVIEW_LENGTH) + "...");
        } else {
            articleDTO.setPreview(articleDTO.getContent());
        }
    }
}
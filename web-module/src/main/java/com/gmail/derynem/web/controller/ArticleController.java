package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.ArticleService;
import com.gmail.derynem.service.CommentService;
import com.gmail.derynem.service.exception.ArticleServiceException;
import com.gmail.derynem.service.exception.CommentServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.article.ArticleDTO;
import com.gmail.derynem.service.model.user.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Arrays;

import static com.gmail.derynem.web.constants.PageNamesConstant.ARTICLES_PAGE;
import static com.gmail.derynem.web.constants.PageNamesConstant.ARTICLE_ADD_PAGE;
import static com.gmail.derynem.web.constants.PageNamesConstant.ARTICLE_PAGE;
import static com.gmail.derynem.web.constants.RedirectConstant.*;

@Controller
public class ArticleController {
    private final static Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService,
                             CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/public/articles")
    public String showArticles(Model model,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                               Authentication authentication) {
        PageDTO<ArticleDTO> articlePageInfo = articleService.getArticlePageInfo(page, limit);
        articlePageInfo.setLimit(limit);
        articlePageInfo.setPage(page);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        model.addAttribute("user", userPrincipal.getUser());
        model.addAttribute("page", articlePageInfo);
        return ARTICLES_PAGE;
    }

    @GetMapping("/public/article/{id}")
    public String showArticle(Model model,
                              @PathVariable(name = "id") Long id,
                              Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        ArticleDTO article = articleService.getArticleById(id);
        if (article == null) {
            return REDIRECT_ARTICLES_PAGE;
        }
        model.addAttribute("userId", userPrincipal.getUser().getId());
        model.addAttribute("article", article);
        return ARTICLE_PAGE;
    }

    @PostMapping("/public/article/comment/delete")
    public String deleteCommentFromArticle(@RequestParam(name = "id") Long id,
                                           @RequestParam(name = "articleId") Long articleId) {
        try {
            commentService.deleteComment(id);
            return String.format(REDIRECT_ARTICLE_PAGE, articleId);
        } catch (CommentServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_NOT_FOUND;
        }
    }

    @PostMapping("/private/article/delete")
    public String deleteArticle(@RequestParam(name = "id") Long id) {
        try {
            articleService.deleteArticle(id);
            return REDIRECT_ARTICLES_PAGE;
        } catch (ArticleServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_NOT_FOUND;
        }
    }

    @PostMapping("/private/article/update")
    public String updateArticle(@ModelAttribute(value = "article") @Valid ArticleDTO article,
                                BindingResult bindingResult,
                                Model model,
                                Authentication authentication) {
        if (bindingResult.hasErrors()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            model.addAttribute("userId", userPrincipal.getUser().getId());
            logger.info(" article not valid , errors :{}", Arrays.toString(bindingResult.getAllErrors().toArray()));
            return ARTICLE_PAGE;
        }
        try {
            articleService.updateArticle(article);
            return String.format(REDIRECT_ARTICLE_PAGE, article.getId());
        } catch (ArticleServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_ARTICLES_PAGE;
        }
    }

    @GetMapping("/private/article/new")
    public String addArticlePage(Model model,
                                 ArticleDTO articleDTO) {
        model.addAttribute("article", articleDTO);
        return ARTICLE_ADD_PAGE;
    }

    @PostMapping("/private/article/new")
    public String saveArticle(@ModelAttribute(value = "article") @Valid ArticleDTO article,
                              BindingResult bindingResult,
                              Authentication authentication) {
        if (bindingResult.hasErrors()) {
            logger.info(" article not valid, errors :{}", Arrays.toString(bindingResult.getAllErrors().toArray()));
            return ARTICLE_ADD_PAGE;
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        article.getUser().setId(userPrincipal.getUser().getId());
        try {
            articleService.saveArticle(article);
            return REDIRECT_ARTICLES_PAGE;
        } catch (ArticleServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_CUSTOM_ERROR;
        }
    }
}
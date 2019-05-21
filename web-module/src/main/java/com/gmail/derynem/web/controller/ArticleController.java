package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.ArticleService;
import com.gmail.derynem.service.CommentService;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.article.ArticleDTO;
import com.gmail.derynem.service.model.user.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.gmail.derynem.web.constants.PageNamesConstant.ARTICLES_PAGE;
import static com.gmail.derynem.web.constants.PageNamesConstant.ARTICLE_PAGE;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_ARTICLES_PAGES;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_ARTICLE_PAGE;

@Controller
public class ArticleController {
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
                               @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        PageDTO<ArticleDTO> articlePageInfo = articleService.getArticlePageInfo(page, limit);
        articlePageInfo.setLimit(limit);
        articlePageInfo.setPage(page);
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
            return REDIRECT_ARTICLES_PAGES;
        }
        model.addAttribute("userId", userPrincipal.getUser().getId());
        model.addAttribute("article", article);
        return ARTICLE_PAGE;
    }

    @PostMapping("/public/article/comment")
    public String deleteCommentFromArticle(@RequestParam(name = "id") Long id,
                                           @RequestParam(name = "articleId") Long articleId) {
        commentService.deleteComment(id);
        return String.format(REDIRECT_ARTICLE_PAGE, articleId);
    }
}
package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.ArticleService;
import com.gmail.derynem.service.CommentService;
import com.gmail.derynem.service.exception.ArticleServiceException;
import com.gmail.derynem.service.exception.CommentServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.article.ArticleDTO;
import com.gmail.derynem.service.model.comment.CommentDTO;
import com.gmail.derynem.service.model.user.UserDTO;
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
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_LIMIT;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_PAGE;
import static com.gmail.derynem.web.constants.PageParamConstant.MESSAGE_PARAM;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_ARTICLES_PAGE;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_ARTICLE_PAGE;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_CUSTOM_ERROR;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_NOT_FOUND;

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
                               @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
                               @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
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
                              Authentication authentication,
                              CommentDTO comment) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        try {
            ArticleDTO article = articleService.getArticleById(id);
            model.addAttribute("user", userPrincipal.getUser());
            model.addAttribute("article", article);
            model.addAttribute("comment", comment);
            return ARTICLE_PAGE;
        } catch (ArticleServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_ARTICLES_PAGE;
        }
    }

    @PostMapping("/public/article/{id}/comment")
    public String addCommentInArticle(@ModelAttribute(value = "comment") @Valid CommentDTO commentDTO,
                                      @PathVariable(value = "id") Long id,
                                      BindingResult bindingResult,
                                      Model model,
                                      Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserDTO currentUser = userPrincipal.getUser();
        if (bindingResult.hasErrors()) {
            try {
                logger.info("comment not valid , errors :{}", Arrays.toString(bindingResult.getAllErrors().toArray()));
                model.addAttribute("user", currentUser);
                ArticleDTO article = articleService.getArticleById(id);
                model.addAttribute("article", article);
                return ARTICLE_PAGE;
            } catch (ArticleServiceException e) {
                logger.error(e.getMessage(), e);
                return REDIRECT_ARTICLES_PAGE + "?message=article added";
            }
        }
        commentDTO.getUser().setId(currentUser.getId());
        commentService.addComment(commentDTO, id);
        return String.format(REDIRECT_ARTICLE_PAGE, id) + String.format(MESSAGE_PARAM, "comment added");
    }

    @PostMapping("/public/article/comment/delete")
    public String deleteCommentFromArticle(@RequestParam(name = "id") Long id,
                                           @RequestParam(name = "articleId") Long articleId) {
        try {
            commentService.deleteComment(id);
            return String.format(REDIRECT_ARTICLE_PAGE, articleId) + String.format(MESSAGE_PARAM, "comment deleted");
        } catch (CommentServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_NOT_FOUND;
        }
    }

    @PostMapping("/private/article/delete")
    public String deleteArticle(@RequestParam(name = "id") Long id) {
        try {
            articleService.deleteArticle(id);
            return REDIRECT_ARTICLES_PAGE + String.format(MESSAGE_PARAM, "article deleted");
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
            model.addAttribute("user", userPrincipal.getUser());
            logger.info(" article not valid , errors :{}", Arrays.toString(bindingResult.getAllErrors().toArray()));
            return ARTICLE_PAGE;
        }
        try {
            articleService.updateArticle(article);
            return String.format(REDIRECT_ARTICLE_PAGE, article.getId()) + String.format(MESSAGE_PARAM, "article updated");
        } catch (ArticleServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_ARTICLES_PAGE + String.format(MESSAGE_PARAM, "update fail");
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
            return REDIRECT_ARTICLES_PAGE + String.format(MESSAGE_PARAM, "article added");
        } catch (ArticleServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_CUSTOM_ERROR;
        }
    }
}
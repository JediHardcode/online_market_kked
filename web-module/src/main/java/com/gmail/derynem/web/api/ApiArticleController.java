package com.gmail.derynem.web.api;

import com.gmail.derynem.service.ArticleService;
import com.gmail.derynem.service.exception.ArticleServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.article.ArticleDTO;
import com.gmail.derynem.service.model.user.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_LIMIT;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_PAGE;

@RestController
@RequestMapping("/api/v1")
public class ApiArticleController {
    private final static Logger logger = LoggerFactory.getLogger(ApiArticleController.class);
    private final ArticleService articleService;

    public ApiArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    public ResponseEntity addArticle(@RequestBody @Valid ArticleDTO articleDTO,
                                     BindingResult bindingResult,
                                     Authentication authentication) {
        if (bindingResult.hasErrors()) {
            logger.info(" article from request not valid cause {} ", Arrays.toString(bindingResult.getAllErrors().toArray()));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            try {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                articleDTO.getUser().setId(userPrincipal.getUser().getId());
                articleService.saveArticle(articleDTO);
                logger.info("article valid and add to database");
                return new ResponseEntity(HttpStatus.CREATED);
            } catch (ArticleServiceException e) {
                logger.error(e.getMessage(), e);
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDTO> getArticleWithId(@PathVariable(name = "id") Long id) {
        try {
            ArticleDTO articleDTO = articleService.getArticleById(id);
            return new ResponseEntity<>(articleDTO, HttpStatus.OK);
        } catch (ArticleServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getListOfArticles
            (@RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
             @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) Integer limit) {
        PageDTO<ArticleDTO> articlePage = articleService.getArticlePageInfo(page, limit);
        return new ResponseEntity<>(articlePage.getEntities(), HttpStatus.OK);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity deleteArticle(@PathVariable(name = "id") Long id) {
        try {
            articleService.deleteArticle(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ArticleServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
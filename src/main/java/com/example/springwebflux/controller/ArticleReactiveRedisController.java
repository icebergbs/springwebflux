package com.example.springwebflux.controller;

import com.example.springwebflux.entity.Article;
import com.example.springwebflux.service.ArticleReactiveRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/articles")
public class ArticleReactiveRedisController {

    @Autowired
    private ArticleReactiveRedisRepository articleReactiveRedisRepository;

    @PostMapping()
    Mono<Boolean> save(@RequestBody Article article){
        return articleReactiveRedisRepository.saveArticle(article);
    }

    @PutMapping()
    Mono<Boolean> update(@RequestBody Article article){
        return articleReactiveRedisRepository.updateArticel(article);
    }


    @GetMapping("/{articleId}")
    Mono<Object> findArticleById(@PathVariable("articleId") final String articleId) {
        return articleReactiveRedisRepository.findArticleById(articleId);
    }

    @GetMapping()
    Flux<Object> findAllArticle() {
        return articleReactiveRedisRepository.findAllArticles().log("findAllArticles");
    }


}

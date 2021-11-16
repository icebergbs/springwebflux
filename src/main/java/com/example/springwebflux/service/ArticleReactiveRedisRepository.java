package com.example.springwebflux.service;


import com.example.springwebflux.entity.Article;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArticleReactiveRedisRepository {

    Mono<Boolean> saveArticle(Article article);

    Mono<Boolean> updateArticel(Article article);

    Mono<Object> findArticleById(String articleId);

    Flux<Object> findAllArticles();
}

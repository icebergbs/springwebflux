package reactor3.springwebflux.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.entity.Article;

public interface ArticleReactiveRedisRepository {

    Mono<Boolean> saveArticle(Article article);

    Mono<Boolean> updateArticel(Article article);

    Mono<Object> findArticleById(String articleId);

    Flux<Object> findAllArticles();
}

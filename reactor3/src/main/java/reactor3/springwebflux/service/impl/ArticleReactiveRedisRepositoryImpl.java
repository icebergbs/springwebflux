package reactor3.springwebflux.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor3.springwebflux.entity.Article;
import reactor3.springwebflux.service.ArticleReactiveRedisRepository;

import javax.annotation.Resource;

@Service
public class ArticleReactiveRedisRepositoryImpl implements ArticleReactiveRedisRepository {

    @Resource
    private ReactiveRedisTemplate<String, Object > reactiveRedisTemplate;

    private static final String HASH_NAME = "Artivle:";

    @Override
    public Mono<Boolean> saveArticle(Article article) {
        return reactiveRedisTemplate.opsForValue().set(HASH_NAME + article.getId(), article);

    }

    @Override
    public Mono<Boolean> updateArticel(Article article) {
        return reactiveRedisTemplate.opsForValue().set(HASH_NAME + article.getId(), article);
    }

    @Override
    public Mono<Object> findArticleById(String articleId) {
        return reactiveRedisTemplate.opsForValue().get(HASH_NAME + articleId);
    }

    @Override
    public Flux<Object> findAllArticles() {
        return reactiveRedisTemplate.keys(HASH_NAME + "*").flatMap((String key) -> {
            Mono<Object> mono = reactiveRedisTemplate.opsForValue().get(key);
            return mono;
        });
    }
}

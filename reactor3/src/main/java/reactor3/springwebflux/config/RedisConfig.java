package reactor3.springwebflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import reactor3.springwebflux.entity.Article;

@Component
public class RedisConfig {

    /**
     * LettuceConnectionFactory
     *  基于Netty创建连接实例,可以在多个线程间实现线程安全,满足多线程环境下的并发访问要求。
     *  同时支持响应式的数据访问用法,也是ReactiveRedisConnectionFactory的一种实现。
     *  luttuce-core组件 依赖reactor-core 和 netty组件的原因
     *
     *  LettuceConnectionFactory 提供了一系列配置项供初始化时进行设置。
     */
    @Bean
    @Primary
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    ReactiveRedisTemplate<String, Article> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Article.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Article> builder = RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Article> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    /**
     * 与传统的区别， 依赖于ReactiveRedisConnectionFactory来获取ReactiveRedisConnection
     * @param factory
     * @return
     */
    @Bean
    ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        return new ReactiveRedisTemplate<String, Object >(factory, RedisSerializationContext.fromSerializer(serializer));
    }


}

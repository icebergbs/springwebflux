package com.bingshan.gateway.filter;

import com.bingshan.judge.ConditionData;
import com.bingshan.strategy.MatchStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * shenyu-spi 测试
 * @author bingshan
 * @date 2022/11/24 11:18
 */
@Slf4j
//@Configuration
public class SpiFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Integer matchMode = 1;
        List<ConditionData> list = new ArrayList<>();
        ConditionData cd1 = new ConditionData();
        cd1.setParamType("uri");
        cd1.setOperator("match");
        cd1.setParamName("/");
        cd1.setParamValue("/http/**");
        list.add(cd1);
        boolean resMatch = MatchStrategyFactory.match(matchMode, list, exchange);
        log.info("SpiFilter :{}", resMatch);

        return chain.filter(exchange);
    }
}

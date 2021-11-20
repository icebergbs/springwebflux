package com.bingshan.user.controller;

import com.bingshan.user.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/account")
public class UserController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/{id}")
    public Mono<Account> getAccount(@PathVariable("id") final String id) {
        Mono<Account>  accountMono = webClientBuilder.build().get()
                .uri("http://account-server/accounts/{accountId}", id)
                .retrieve().bodyToMono(Account.class);
        return accountMono;
    }
}

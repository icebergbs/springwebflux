package com.bingshan.account.controller;

import com.bingshan.account.entity.Account;
import com.bingshan.account.server.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @GetMapping("/{accountId}")
    public Mono<Account> findAccountById(@PathVariable("accountId") String accountId, ServerHttpRequest request) {
        log.info("findAccountById : id = {} from port: {}", accountId, request.getURI());
        return accountService.getAccountsById(accountId);
    }

    @PostMapping
    public Mono<Void> createAccount(@RequestBody final Mono<Account> account) {
        log.info("createAccount : account = {}", account);
        return accountService.createOrUpdateAccount(account);
    }

    @GetMapping("/testException")
    public Mono<String> testException() {
        try {
            float a = 1/0;
        } catch (Exception e) {
            throw new LinkerRuntimeException(200, "Test Exception!");
        }
        return Mono.just("Test Exception");
    }

}

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

import javax.xml.bind.SchemaOutputResolver;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private WebClient.Builder webClientBuilder;
//
//    @GetMapping("/{id}")
//    public Mono<Account> getAccount(@PathVariable("id") final String id) {
//        Mono<Account>  accountMono = webClientBuilder.build().get()
//                .uri("http://account-server/accounts/{accountId}", id)
//                .retrieve().bodyToMono(Account.class);
//        log.info("users-server:  getAccount() id = {} ", id);
//
//        return accountMono;
//    }

    @GetMapping("/account1")
    public Account getAccount1( ) {
        Account account = new Account();
        account.setAccountId("1");
        account.setAccountName("xx");
        account.setPHoNe("xxxx");

        Account account1 = new Account();
        account1.setAccountId("12");
        account1.setAccountName("xx2");
        account1.setPHoNe("xxxx2");
        account.setAccount(account1);
        return account;
    }

    @GetMapping("/account")
    public Mono<Account> getAccount11( ) {
        Account account = new Account();
        account.setAccountId("1");
        account.setAccountName("xx");
        account.setPHoNe("xxxx");
        Mono<Account> accountMono = webClientBuilder.build()
                .get()
                .uri("http://account-server/accounts/{accountId}", "1")
                .retrieve()
                .bodyToMono(Account.class);
        accountMono.switchIfEmpty(Mono.just(account))
                .flatMap(obj ->  Mono.just(obj))
                .subscribe(System.out::println);

        log.info("users-server:  getAccount()", accountMono);

        return accountMono;
    }
}

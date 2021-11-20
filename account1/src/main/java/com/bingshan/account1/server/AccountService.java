package com.bingshan.account1.server;

import com.bingshan.account1.entity.Account;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {

    private static Map<String, Account> accountMap = new ConcurrentHashMap<>();
    static {
        Account account = new Account();
        account.setAccountId("1");
        account.setAccountName("zhanghao name");
        account.setPhone("110");
        accountMap.put(account.getAccountId(), account);
    }

    public Flux<Account> getAccount() {
        return Flux.fromIterable(this.accountMap.values());
    }

    public Flux<Account> getAccountsById(final Flux<String> ids) {
        return ids.flatMap(id -> Mono.justOrEmpty(this.accountMap.get(id)));
    }

    public Mono<Account> getAccountsById(final String id) {
        return Mono.justOrEmpty(this.accountMap.get(id));
    }

    public Mono<Void> createOrUpdateAccount(final Mono<Account> accountMono) {
        return accountMono.doOnNext(account -> {
            accountMap.put(account.getAccountId(), account);
        }).thenEmpty(Mono.empty());
    }

    public Mono<Account> deleteAccount(final String id) {
        return Mono.justOrEmpty(this.accountMap.remove(id));
    }

}

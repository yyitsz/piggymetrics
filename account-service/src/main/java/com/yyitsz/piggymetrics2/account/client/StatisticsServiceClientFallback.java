package com.yyitsz.piggymetrics2.account.client;


import com.yyitsz.piggymetrics2.account.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author cdov
 */
//@Component
@Slf4j
public class StatisticsServiceClientFallback implements StatisticsServiceClient {

    @Override
    public CompletableFuture<Void> updateStatistics(String accountName, Account account) {
        log.error("Error during update statistics for account: {}", accountName);
        return CompletableFuture.completedFuture(null);
    }
}

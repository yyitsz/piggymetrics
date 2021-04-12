package com.yyitsz.piggymetrics2.account.client;


import com.yyitsz.piggymetrics2.account.domain.Account;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@FeignClient(name = "statistics-service", fallbackFactory = StatisticsServiceClientFallbackFactory.class)
public interface StatisticsServiceClient {

   // @RateLimiter(name = "statistics-service")
    //@RateLimiter(name = "statistics-service")
    //@Bulkhead(name = "statistics-service", type = Bulkhead.Type.THREADPOOL)
    //@Retry(name = "statistics-service")
    //@TimeLimiter(name = "statistics-service")
    @RequestMapping(method = RequestMethod.PUT, value = "/statistics/{accountName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    CompletableFuture<Void> updateStatistics(@PathVariable("accountName") String accountName, Account account);

}

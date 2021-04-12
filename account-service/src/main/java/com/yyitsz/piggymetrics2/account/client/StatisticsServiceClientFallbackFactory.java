package com.yyitsz.piggymetrics2.account.client;


import com.yyitsz.piggymetrics2.account.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author cdov
 */
@Component
@Slf4j
public class StatisticsServiceClientFallbackFactory implements FallbackFactory<StatisticsServiceClient> {

    @Override
    public StatisticsServiceClient create(Throwable cause) {
        log.error("StatisticsService error.", cause);
        return new StatisticsServiceClientFallback();
    }
}

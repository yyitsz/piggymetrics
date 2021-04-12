package com.yyitsz.piggymetrics2.account.client;

import com.yyitsz.piggymetrics2.account.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthServiceClientFallbackFactory implements FallbackFactory<AuthServiceClient> {

    @Override
    public AuthServiceClient create(Throwable cause) {
        log.error("AuthService Error.", cause);
        return new AuthServiceClientFallback();
    }
}

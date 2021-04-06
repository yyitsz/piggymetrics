package com.yyitsz.piggymetrics2.account.client;

import com.yyitsz.piggymetrics2.account.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthServiceClientFallback implements AuthServiceClient {
    @Override
    public void createUser(User user) {
        log.error("Error during create user {}", user);
    }
}

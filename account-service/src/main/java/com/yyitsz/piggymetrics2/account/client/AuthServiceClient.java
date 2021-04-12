package com.yyitsz.piggymetrics2.account.client;


import com.yyitsz.piggymetrics2.account.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "auth-service", fallbackFactory = AuthServiceClientFallbackFactory.class)
public interface AuthServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/uaa/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createUser(User user);

}

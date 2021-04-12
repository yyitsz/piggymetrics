package com.yyitsz.piggymetrics2.statistics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ResourceServerConfig {

    // formatter:off
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .mvcMatcher("/**")
                .authorizeRequests()
                .mvcMatchers("/**").access("hasAuthority('SCOPE_server')")
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
    // formatter:on

}
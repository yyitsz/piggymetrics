package com.yyitsz.piggymetrics2.account.config;

import com.yyitsz.piggymetrics2.common.oauth2.OAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author cdov
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class ResourceServerConfig {
    @Bean
    SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/demo").permitAll()
                .and()
                .authorizeRequests().anyRequest().permitAll()
        ;
//        http
//                .mvcMatcher("/**")
//                .authorizeRequests()
//                .mvcMatchers("/**").access("hasAuthority('SCOPE_server')")
//                .and()
//                .oauth2ResourceServer()
//                .jwt();

        //        http.authorizeRequests()
//                .mvcMatchers("/", "/demo").access("hasAuthority('SCOPE_server')")
//                .and()
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2ResourceServer()
//                .jwt()
//        ;

        return http.build();
    }

    @Bean
    WebSecurityCustomizer configureWebSecurity() throws Exception {
        return web -> {
            //web.ignoring().antMatchers("/console/**", "/actuator/**");
            web.ignoring().antMatchers("/**");
        };
    }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken()
                        .clientCredentials()
                        .build();
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    OAuthRequestInterceptor oAuthRequestInterceptor(OAuth2AuthorizedClientManager clientManager) {
        return new OAuthRequestInterceptor(clientManager, "account-service");
    }
}


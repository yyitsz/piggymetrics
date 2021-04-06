package com.yyitsz.piggymetrics2.notification.config;

//import feign.RequestInterceptor;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//

/**
 * @author cdov
 */
//@EnableWebSecurity
//@Configuration
//public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
//    @Bean
//    @ConfigurationProperties(prefix = "security.oauth2.client")
//    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
//        return new ClientCredentialsResourceDetails();
//    }
//
//    @Bean
//    public RequestInterceptor oauth2FeignRequestInterceptor() {
//        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
//    }
//
//    @Bean
//    public OAuth2RestTemplate clientCredentialsRestTemplate() {
//        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .mvcMatcher("/messages/**")
//                .authorizeRequests()
//                .mvcMatchers("/messages/**").access("hasAuthority('SCOPE_message.read')")
//                .and()
//                .oauth2ResourceServer()
//                .jwt();
//    }
//}

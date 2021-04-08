/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.piggymetrics.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//        return http.formLogin(Customizer.withDefaults()).build();
//    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        List<RegisteredClient> registeredClientList = new ArrayList<>();
//        {
//            //example
//            RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                    .clientId("messaging-client")
//                    .clientSecret("secret")
//                    .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                    .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
//                    .redirectUri("http://127.0.0.1:8080/authorized")
//                    .scope(OidcScopes.OPENID)
//                    .scope("message.read")
//                    .scope("message.write")
//                    .clientSettings(clientSettings -> clientSettings.requireUserConsent(true))
//                    .build();
//            registeredClientList.add(registeredClient);
//        }
        {
            RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("browser")
                    .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .scope("ui")
                    .build();
            registeredClientList.add(registeredClient);
        }
        {
            RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("account-service")
                    .clientSecret("password")
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .scope("server")
                    .build();
            registeredClientList.add(registeredClient);
        }
        {
            RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("statistics-service")
                    .clientSecret("password")
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .scope("server")
                    .build();
            registeredClientList.add(registeredClient);
        }
        {
            RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("notification-service")
                    .clientSecret("password")
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .scope("server")
                    .build();
            registeredClientList.add(registeredClient);
        }

        return new InMemoryRegisteredClientRepository(registeredClientList);
    }


    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public ProviderSettings providerSettings() {
        return new ProviderSettings().issuer("http://auth-service:7090");
    }
}

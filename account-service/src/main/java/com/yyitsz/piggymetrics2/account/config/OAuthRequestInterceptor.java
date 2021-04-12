package com.yyitsz.piggymetrics2.account.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class OAuthRequestInterceptor implements RequestInterceptor {

    // Same key as in
    // SecurityReactorContextConfiguration.SecurityReactorContextSubscriber.SECURITY_CONTEXT_ATTRIBUTES
    static final String SECURITY_REACTOR_CONTEXT_ATTRIBUTES_KEY = "org.springframework.security.SECURITY_CONTEXT_ATTRIBUTES";

    /**
     * The request attribute name used to locate the {@link OAuth2AuthorizedClient}.
     */
    private static final String OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME = OAuth2AuthorizedClient.class.getName();

    private static final String CLIENT_REGISTRATION_ID_ATTR_NAME = OAuth2AuthorizedClient.class.getName()
            .concat(".CLIENT_REGISTRATION_ID");

    private static final String AUTHENTICATION_ATTR_NAME = Authentication.class.getName();

    private static final String HTTP_SERVLET_REQUEST_ATTR_NAME = HttpServletRequest.class.getName();

    private static final String HTTP_SERVLET_RESPONSE_ATTR_NAME = HttpServletResponse.class.getName();

    private static final Authentication ANONYMOUS_AUTHENTICATION = new AnonymousAuthenticationToken("anonymous",
            "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

    private OAuth2AuthorizedClientManager authorizedClientManager;

    private String defaultClientRegistrationId;
    private boolean defaultOAuth2AuthorizedClient;

    public OAuthRequestInterceptor(OAuth2AuthorizedClientManager manager) {
        this.authorizedClientManager = manager;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, Object> attrs = new ConcurrentHashMap<>();
        populateDefaultRequestResponse(attrs);
        populateDefaultAuthentication(attrs);
        attrs.put(CLIENT_REGISTRATION_ID_ATTR_NAME, "account-service");
        String clientRegistrationId = resolveClientRegistrationId(attrs);
        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizeClient(clientRegistrationId, attrs);
        bearer(requestTemplate, oAuth2AuthorizedClient, attrs);

    }

    private void populateDefaultRequestResponse(Map<String, Object> attrs) {

        RequestAttributes context = RequestContextHolder.getRequestAttributes();
        if (context instanceof ServletRequestAttributes) {
            attrs.putIfAbsent(HTTP_SERVLET_REQUEST_ATTR_NAME, ((ServletRequestAttributes) context).getRequest());
            attrs.putIfAbsent(HTTP_SERVLET_RESPONSE_ATTR_NAME, ((ServletRequestAttributes) context).getResponse());
        }
    }

    private void populateDefaultAuthentication(Map<String, Object> attrs) {
        if (attrs.containsKey(Authentication.class.getName())) {
            return;
        }

        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null) {
            Authentication authentication = context.getAuthentication();
            attrs.putIfAbsent(AUTHENTICATION_ATTR_NAME, authentication);
        }
    }

    static Authentication getAuthentication(Map<String, Object> attrs) {
        return (Authentication) attrs.get(Authentication.class.getName());
    }

    static HttpServletRequest getRequest(Map<String, Object> attrs) {
        return (HttpServletRequest) attrs.get(HttpServletRequest.class.getName());
    }

    static HttpServletResponse getResponse(Map<String, Object> attrs) {
        return (HttpServletResponse) attrs.get(HttpServletResponse.class.getName());
    }

    private static Authentication createAuthentication(final String principalName) {
        Assert.hasText(principalName, "principalName cannot be empty");
        return new AbstractAuthenticationToken(null) {

            @Override
            public Object getCredentials() {
                return "";
            }

            @Override
            public Object getPrincipal() {
                return principalName;
            }
        };
    }

    private void addToAttributes(Map<String, Object> attributes, HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse) {
        if (servletRequest != null) {
            attributes.put(HTTP_SERVLET_REQUEST_ATTR_NAME, servletRequest);
        }
        if (servletResponse != null) {
            attributes.put(HTTP_SERVLET_RESPONSE_ATTR_NAME, servletResponse);
        }
    }

    private void bearer(RequestTemplate request, OAuth2AuthorizedClient authorizedClient, Map<String, Object> attributes) {
        // @formatter:off
        request.header(AUTHORIZATION, "Bearer " + authorizedClient.getAccessToken().getTokenValue());
        oauth2AuthorizedClient(attributes, authorizedClient);
    }

    public static void oauth2AuthorizedClient(Map<String, Object> attributes, OAuth2AuthorizedClient authorizedClient) {

        if (authorizedClient == null) {
            attributes.remove(OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME);
        } else {
            attributes.put(OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME, authorizedClient);
        }

    }

    static String getClientRegistrationId(Map<String, Object> attrs) {
        return (String) attrs.get(CLIENT_REGISTRATION_ID_ATTR_NAME);
    }

    private String resolveClientRegistrationId(Map<String, Object> attrs) {

        String clientRegistrationId = getClientRegistrationId(attrs);
        if (clientRegistrationId == null) {
            clientRegistrationId = this.defaultClientRegistrationId;
        }
        Authentication authentication = getAuthentication(attrs);
        if (clientRegistrationId == null && this.defaultOAuth2AuthorizedClient
                && authentication instanceof OAuth2AuthenticationToken) {
            clientRegistrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        }
        return clientRegistrationId;
    }

    private OAuth2AuthorizedClient authorizeClient(String clientRegistrationId, Map<String, Object> attrs) {
        if (this.authorizedClientManager == null) {
            return null;
        }

        Authentication authentication = getAuthentication(attrs);
        if (authentication == null) {
            authentication = ANONYMOUS_AUTHENTICATION;
        }
        HttpServletRequest servletRequest = getRequest(attrs);
        HttpServletResponse servletResponse = getResponse(attrs);
        OAuth2AuthorizeRequest.Builder builder = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistrationId)
                .principal(authentication);
        builder.attributes((attributes) -> addToAttributes(attributes, servletRequest, servletResponse));
        OAuth2AuthorizeRequest authorizeRequest = builder.build();
        // NOTE: 'authorizedClientManager.authorize()' needs to be executed on a dedicated
        // thread via subscribeOn(Schedulers.boundedElastic()) since it performs a
        // blocking I/O operation using RestTemplate internally
        return this.authorizedClientManager.authorize(authorizeRequest);
    }

    private OAuth2AuthorizedClient reauthorizeClient(OAuth2AuthorizedClient authorizedClient,
                                                     Map<String, Object> attrs) {
        if (!attrs.containsKey(OAUTH2_AUTHORIZED_CLIENT_ATTR_NAME)) {
            return null;
        }
        if (this.authorizedClientManager == null) {
            return authorizedClient;
        }
        // Map<String, Object> attrs = request.attributes();
        Authentication authentication = getAuthentication(attrs);
        if (authentication == null) {
            authentication = createAuthentication(authorizedClient.getPrincipalName());
        }
        HttpServletRequest servletRequest = getRequest(attrs);
        HttpServletResponse servletResponse = getResponse(attrs);
        OAuth2AuthorizeRequest.Builder builder = OAuth2AuthorizeRequest.withAuthorizedClient(authorizedClient)
                .principal(authentication);
        builder.attributes((attributes) -> addToAttributes(attributes, servletRequest, servletResponse));
        OAuth2AuthorizeRequest reauthorizeRequest = builder.build();
        // NOTE: 'authorizedClientManager.authorize()' needs to be executed on a dedicated
        // thread via subscribeOn(Schedulers.boundedElastic()) since it performs a
        // blocking I/O operation using RestTemplate internally
        return this.authorizedClientManager.authorize(reauthorizeRequest);
    }

    private Authentication createPrincipal() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptySet();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return this;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }

            @Override
            public String getName() {
                return "backend";
            }
        };
    }
}

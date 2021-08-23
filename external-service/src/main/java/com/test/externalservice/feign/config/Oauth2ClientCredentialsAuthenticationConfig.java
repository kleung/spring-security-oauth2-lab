package com.test.externalservice.feign.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class Oauth2ClientCredentialsAuthenticationConfig {

    @Value("${authorization.server.clientId}")
    private String clientId;

    @Value("${authorization.server.clientSecret}")
    private String clientSecret;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(this.clientId, this.clientSecret);
    }
}

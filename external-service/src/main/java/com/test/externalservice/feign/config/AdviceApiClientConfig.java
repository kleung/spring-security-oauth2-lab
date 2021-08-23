package com.test.externalservice.feign.config;

import com.test.externalservice.feign.client.Oauth2ClientCredentialsClient;
import com.test.externalservice.feign.interceptor.Oauth2ClientCredentialsInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class AdviceApiClientConfig {

    @Autowired
    private Oauth2ClientCredentialsClient oauth2ClientCredentialsClient;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor oauth2ClientCredentialsInterceptor() {
        return new Oauth2ClientCredentialsInterceptor(this.oauth2ClientCredentialsClient);
    }

}

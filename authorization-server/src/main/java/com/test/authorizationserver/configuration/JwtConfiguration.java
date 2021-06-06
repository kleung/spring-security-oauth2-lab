package com.test.authorizationserver.configuration;

import com.test.authorizationserver.tokenenhancer.SubjectSettingTokenEnhancer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
public class JwtConfiguration {

    @Value("${keystore.location}")
    private Resource keystore;

    @Value("${keypass}")
    private String keystorePassword;

    @Value("${alias}")
    private String alias;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(
                jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(
                        keystore,
                        keystorePassword.toCharArray()
                );

        converter.setKeyPair(
                keyStoreKeyFactory.getKeyPair(alias));

        return converter;
    }

    @Bean
    public SubjectSettingTokenEnhancer subjectSettingTokenEnhancer() {
        return new SubjectSettingTokenEnhancer();
    }

}

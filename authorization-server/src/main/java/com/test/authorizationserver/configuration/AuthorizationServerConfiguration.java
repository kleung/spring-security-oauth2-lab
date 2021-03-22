package com.test.authorizationserver.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;


@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;

    private TokenStore tokenStore;

    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(this.authenticationManager)
                .tokenStore(this.tokenStore)
                .accessTokenConverter(this.jwtAccessTokenConverter);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()
                    .withClient("password_client")
                    .secret("secret")
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("read")
                .and()
                    .withClient("authorization_code_client")
                    .secret("secret")
                    .authorizedGrantTypes("authorization_code", "refresh_token")
                    .scopes("read")
                    .redirectUris("http://localhost:9090/home")
                .and()
                    .withClient("client_credentials_client")
                    .secret("secret")
                    .authorizedGrantTypes("client_credentials")
                    .scopes("info");
    }

}

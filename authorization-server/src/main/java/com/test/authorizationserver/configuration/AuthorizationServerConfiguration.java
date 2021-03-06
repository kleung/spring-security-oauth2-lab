package com.test.authorizationserver.configuration;

import com.test.authorizationserver.tokenenhancer.SubjectSettingTokenEnhancer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final TokenStore tokenStore;

    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    private final UserDetailsService userDetailsService;

    private final DataSource dataSource;

    private final PasswordEncoder passwordEncoder;

    private final SubjectSettingTokenEnhancer subjectSettingTokenEnhancer;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(subjectSettingTokenEnhancer,
                        jwtAccessTokenConverter));

        endpoints
                .authenticationManager(this.authenticationManager)
                .tokenStore(this.tokenStore)
                .tokenEnhancer(tokenEnhancerChain)
                .userDetailsService(this.userDetailsService);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("isAnonymous()")
                .checkTokenAccess("isAuthenticated()");
    }



    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() throws Exception {
        //due to configurer calling sequence, I cannot inject the ClientDetailsServiceConfigurer and call .and().build() here as this bean is built before the configure() is called.
        //even the configure() is called, the bean being built as "clientDetailsService" is a jdk proxy
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(this.dataSource);
        jdbcClientDetailsService.setPasswordEncoder(this.passwordEncoder);

        return jdbcClientDetailsService;
    }

}

package com.test.module5gateway.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@AllArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private ReactiveJwtDecoder jwtDecoder;

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/oauth/token").permitAll()
                .anyExchange().authenticated()
                .and()
        .oauth2ResourceServer()
            .jwt(jwt ->
                    jwt.jwtDecoder(jwtDecoder)
                    .jwtAuthenticationConverter(jwtAuthenticationConverter)
            );

        return http.build();
    }
}

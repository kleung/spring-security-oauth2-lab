package com.laurentiuspilca.liveproject.config;

import com.laurentiuspilca.liveproject.converter.authority.CollectionJwtGrantedAuthoritiesConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

@Configuration
public class JwtConfiguration {

    @Value("${public-key.location}")
    private RSAPublicKey publicKey;

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey)
                .signatureAlgorithm(SignatureAlgorithm.RS256).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        //authorities converter
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("authorities");
        authoritiesConverter.setAuthorityPrefix("");

        //scope converter
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();

        CollectionJwtGrantedAuthoritiesConverter collectionJwtGrantedAuthoritiesConverter =
                new CollectionJwtGrantedAuthoritiesConverter(Arrays.asList(
                        authoritiesConverter,
                        scopeConverter
                ));

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(collectionJwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}

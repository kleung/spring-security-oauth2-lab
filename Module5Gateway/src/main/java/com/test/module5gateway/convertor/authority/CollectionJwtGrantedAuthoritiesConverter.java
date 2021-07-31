package com.test.module5gateway.convertor.authority;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CollectionJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private Collection<JwtGrantedAuthoritiesConverter> jwtGrantedAuthoritiesConverterList;

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        return this.jwtGrantedAuthoritiesConverterList.stream()
                .map(converter -> converter.convert(jwt))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
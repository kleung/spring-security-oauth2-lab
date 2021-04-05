package com.test.authorizationserver.mapper;

import com.test.authorizationserver.model.userdetail.JpaAuthority;
import com.test.authorizationserver.model.entity.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface JpaAuthorityMapper {

    @Mapping(source = "authority.authority", target = "authority")
    JpaAuthority mapAuthority(Authority authority);

    @Mapping(source = "authority", target = "authority")
    JpaAuthority mapAuthority(String authority);
}

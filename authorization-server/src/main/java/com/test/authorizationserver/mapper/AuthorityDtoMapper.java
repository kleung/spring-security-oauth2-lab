package com.test.authorizationserver.mapper;

import com.test.authorizationserver.model.entity.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuthorityDtoMapper {

    @Mapping(source = "authority", target = "authority")
    Authority mapAuthorityDto(String authority);

}

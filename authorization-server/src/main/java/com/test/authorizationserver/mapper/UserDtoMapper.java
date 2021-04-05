package com.test.authorizationserver.mapper;

import com.test.authorizationserver.model.dto.UserDto;
import com.test.authorizationserver.model.entity.Authority;
import com.test.authorizationserver.model.entity.User;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = AuthorityDtoMapper.class)
public interface UserDtoMapper {

    @Named("mapAuthorityToDtoAuthority")
    default List<String> mapAuthorityToDtoAuthority(List<Authority> authorityList) {
        List<String> result = new ArrayList<>();

        if(authorityList != null) {
            result = authorityList.stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.toList());
        }

        return result;
    }

    @Mappings({
            @Mapping(source = "user.id", target = "id"),
            @Mapping(source = "user.username", target = "username"),
            @Mapping(target = "password", ignore = true), //the password hash should not leave the api; probably it is the most performant to ignore the field here
            @Mapping(source = "user.enabled", target = "enabled"),
            @Mapping(target = "authorities", qualifiedByName = "mapAuthorityToDtoAuthority")
    })
    UserDto mapUser(User user);

    List<UserDto> mapUserList(List<User> user);

    @Mappings({
            @Mapping(source = "userDto.id", target = "id"),
            @Mapping(source = "userDto.username", target = "username"),
            @Mapping(source = "userDto.password", target = "password"),
            @Mapping(source = "userDto.enabled", target = "enabled")
    })
    User mapUserDto(UserDto userDto);

}

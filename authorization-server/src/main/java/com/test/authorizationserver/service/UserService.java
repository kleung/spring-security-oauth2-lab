package com.test.authorizationserver.service;

import com.test.authorizationserver.model.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto persist(UserDto userDto);

    List<UserDto> findAll();

    //should further expand to support update (patch)
}

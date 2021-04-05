package com.test.authorizationserver.service;

import com.test.authorizationserver.model.dto.ClientDetailsDto;

import java.util.List;

public interface ClientDetailsDtoService {

    ClientDetailsDto persist(ClientDetailsDto dto);

    List<ClientDetailsDto> findAll();

}

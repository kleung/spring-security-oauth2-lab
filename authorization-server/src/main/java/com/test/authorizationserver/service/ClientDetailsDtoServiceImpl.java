package com.test.authorizationserver.service;

import com.test.authorizationserver.mapper.ClientDetailsDtoMapper;
import com.test.authorizationserver.model.dto.ClientDetailsDto;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientDetailsDtoServiceImpl implements ClientDetailsDtoService {

    private JdbcClientDetailsService jdbcClientDetailsService;

    private ClientDetailsDtoMapper mapper;

    @Override
    public ClientDetailsDto persist(ClientDetailsDto dto) {
        ClientDetails clientDetails = this.mapper.mapClientDetailsDto(dto);

        this.jdbcClientDetailsService.addClientDetails(clientDetails);
        clientDetails = this.jdbcClientDetailsService.loadClientByClientId(clientDetails.getClientId());

        return this.mapper.mapClientDetails(clientDetails);
    }

    @Override
    public List<ClientDetailsDto> findAll() {
        List<ClientDetails> clientDetailsList = this.jdbcClientDetailsService.listClientDetails();

        return this.mapper.mapClientDetailsList(clientDetailsList);
    }
}

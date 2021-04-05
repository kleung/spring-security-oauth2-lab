package com.test.authorizationserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.authorizationserver.model.dto.ClientDetailsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ClientDetailsDtoServiceTest {

    @Autowired
    private ClientDetailsDtoService clientDetailsDtoService;

    @Autowired
    private JdbcClientDetailsService clientDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:testClient.json")
    private Resource testClientJson;

    @Test
    public void testPersistFindAll() throws Exception {
        //arrange
        ClientDetailsDto testClient = this.objectMapper.readValue(this.testClientJson.getInputStream(),
                ClientDetailsDto.class);

        //act
        this.clientDetailsDtoService.persist(testClient);
        List<ClientDetailsDto> allClients = this.clientDetailsDtoService.findAll();

        //assert
        assertNotNull(allClients);
        assertTrue(allClients.size() > 0);

        testClient = allClients.stream()
                .filter(client -> client.getClientId().equalsIgnoreCase("test_client"))
                .findFirst()
                .orElse(null);
        assertNotNull(testClient);

        this.clientDetailsService.removeClientDetails("test_client");
    }
}

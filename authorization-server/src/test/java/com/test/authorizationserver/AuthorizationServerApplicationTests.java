package com.test.authorizationserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.authorizationserver.model.dto.ClientDetailsDto;
import com.test.authorizationserver.model.dto.UserDto;
import com.test.authorizationserver.repository.UserRepository;
import com.test.authorizationserver.service.ClientDetailsDtoService;
import com.test.authorizationserver.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationServerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcClientDetailsService clientDetailsService;

    @Autowired
    private ClientDetailsDtoService clientDetailsDtoService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:clientCredentialClient.json")
    private Resource clientCredentialClientJson;

    @Value("classpath:passwordClient.json")
    private Resource passwordClientJson;

    @Value("classpath:user.json")
    private Resource userJson;

    @BeforeEach
    public void init() throws Exception {
        if(this.userRepository.findByUsername("ken") == null) {
            UserDto user = this.objectMapper.readValue(this.userJson.getInputStream(),
                    UserDto.class);
            this.userService.persist(user);
        }

        try {
            this.clientDetailsService.loadClientByClientId("password_client");
        } catch (NoSuchClientException nsce) {
            ClientDetailsDto passwordClient = this.objectMapper.readValue(this.passwordClientJson.getInputStream(),
                    ClientDetailsDto.class);
            this.clientDetailsDtoService.persist(passwordClient);
        }

        try {
            this.clientDetailsService.loadClientByClientId("client_credentials_client");
        } catch (NoSuchClientException nsce) {
            ClientDetailsDto clientCredentialsClient = this.objectMapper.readValue(this.clientCredentialClientJson.getInputStream(),
                    ClientDetailsDto.class);
            this.clientDetailsDtoService.persist(clientCredentialsClient);
        }
    }

    @Test
    void testPasswordFlowAuthentication() throws Exception {
        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic("password_client", "secret"))
                .param("grant_type", "password")
                .param("scope", "read")
                .param("username", "ken")
                .param("password", "12345")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").isNotEmpty());
    }

    @Test
    void testClientCredentialFlowAuthentication() throws Exception {
        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic("client_credentials_client", "secret"))
                .param("grant_type", "client_credentials")
                .param("scope", "info")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").isNotEmpty());
    }

    //not sure how to test authorization code


}

package com.test.authorizationserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    //PROBLEM: getting invalid token error with "Cannot convert access token to JSON" for refresh token...

}

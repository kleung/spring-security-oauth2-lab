package com.test.module5gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@ActiveProfiles({"test"})
@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
class Module5GatewayApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Value("classpath:sample-jwt-token.txt")
    private Resource sampleJwtTokenTxt;

    @Test
    public void testUnauthenticatedKnownRouteShouldFail() throws Exception {
        stubFor(get(urlPathMatching("/metric/([a-z]*)"))
                .willReturn(aResponse().withBody("hello")));

        this.webTestClient
                .get()
                .uri("/metric/abc")
                .header("content-type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void testUnknownRouteShouldReturn404() throws Exception {
        this.webTestClient
                .mutateWith(mockJwt().authorities(() -> "admin"))
                .get()
                .uri("/unknown")
                .header("content-type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testKnownRouteShouldPass() throws Exception {
        stubFor(get(urlPathMatching("/metric/([a-z]*)"))
            .willReturn(aResponse().withBody("hello")));

        this.webTestClient
                .mutateWith(mockJwt().jwt(Jwt.withTokenValue(StreamUtils.copyToString(this.sampleJwtTokenTxt.getInputStream(), Charset.defaultCharset()))
                        .subject("ken")
                        .header("typ", "JWT")
                        .build()))
                .get()
                .uri("/metric/abc")
                .header("content-type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("hello");
    }

}

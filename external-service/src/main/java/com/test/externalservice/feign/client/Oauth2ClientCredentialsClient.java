package com.test.externalservice.feign.client;

import com.test.externalservice.feign.config.Oauth2ClientCredentialsAuthenticationConfig;
import com.test.externalservice.model.AuthorizationServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "oauth2-client-credentials-client",
        url = "${authorization.server.url}",
        configuration = {Oauth2ClientCredentialsAuthenticationConfig.class},
        primary = false
)
public interface Oauth2ClientCredentialsClient {

    @PostMapping
    AuthorizationServerResponse getToken();

}

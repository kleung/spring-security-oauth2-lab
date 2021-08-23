package com.test.externalservice.feign.interceptor;

import com.test.externalservice.feign.client.Oauth2ClientCredentialsClient;
import com.test.externalservice.model.AuthorizationServerResponse;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Oauth2ClientCredentialsInterceptor implements RequestInterceptor {

    private Oauth2ClientCredentialsClient oauth2ClientCredentialsClient;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        AuthorizationServerResponse tokenResponse = this.oauth2ClientCredentialsClient.getToken();
        String token = tokenResponse.getAccessToken();

        String tokenHeaderValueTemplate = "Bearer %s";
        String tokenHeaderValue = String.format(tokenHeaderValueTemplate, token);

        requestTemplate.header("Authorization", new String[]{ tokenHeaderValue });
    }
}

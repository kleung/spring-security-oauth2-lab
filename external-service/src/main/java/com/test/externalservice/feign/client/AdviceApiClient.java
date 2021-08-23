package com.test.externalservice.feign.client;

import com.test.externalservice.feign.config.AdviceApiClientConfig;
import com.test.externalservice.model.HealthAdvice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "advice-api-client",
        url = "${api.gateway.url}",
        configuration = {AdviceApiClientConfig.class},
        primary = false
)
public interface AdviceApiClient {

    @PostMapping(value = "/advice")
    void provideHealthAdviceCallback(@RequestBody List<HealthAdvice> healthAdvice);
}

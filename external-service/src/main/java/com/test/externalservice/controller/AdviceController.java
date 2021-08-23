package com.test.externalservice.controller;

import com.test.externalservice.feign.client.AdviceApiClient;
import com.test.externalservice.model.HealthAdvice;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class AdviceController {

    private AdviceApiClient client;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/advice")
    public void provideHealthAdviceCallback(@RequestBody List<HealthAdvice> healthAdvice) {
        this.client.provideHealthAdviceCallback(healthAdvice);
    }

}

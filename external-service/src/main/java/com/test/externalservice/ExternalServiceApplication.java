package com.test.externalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ExternalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalServiceApplication.class, args);
    }

}

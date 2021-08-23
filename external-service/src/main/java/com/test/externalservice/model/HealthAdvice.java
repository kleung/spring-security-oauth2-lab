package com.test.externalservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HealthAdvice {

    private String advice;

    private String username;
}

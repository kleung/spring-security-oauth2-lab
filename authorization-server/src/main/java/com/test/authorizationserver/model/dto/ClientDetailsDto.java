package com.test.authorizationserver.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class ClientDetailsDto {

    @NotEmpty
    @Size(min = 1, max = 256)
    private String clientId;

    @NotEmpty
    private String clientSecret;

    private List<String> resourceIds;

    @NotEmpty
    private List<String> scope;

    private List<String> authorizedGrantTypes;

    private List<String> registeredRedirectUri;

    private List<String> authorities;

}

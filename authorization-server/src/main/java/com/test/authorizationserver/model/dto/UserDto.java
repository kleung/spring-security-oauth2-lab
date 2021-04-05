package com.test.authorizationserver.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private Boolean enabled;

    @NotEmpty
    @Size(min = 1)
    private List<String> authorities;
}

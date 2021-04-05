package com.test.authorizationserver.model.userdetail;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Data
@NoArgsConstructor
public class JpaAuthority implements GrantedAuthority {

    private String authority;

}

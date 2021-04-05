package com.test.authorizationserver.model.userdetail;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@NoArgsConstructor
public class JpaUserDetail implements UserDetails {

    private String username;

    private String password;

    private boolean enabled;

    private Collection<GrantedAuthority> authorities;

    //hard code this for now as db schema was not supporting it...
    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;
}

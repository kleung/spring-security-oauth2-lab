package com.test.authorizationserver.service;

import com.test.authorizationserver.mapper.JpaUserDetailMapper;
import com.test.authorizationserver.repository.UserRepository;
import com.test.authorizationserver.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JpaUserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    private JpaUserDetailMapper userDetailMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return this.userDetailMapper.mapUserDetail(user);
    }


}

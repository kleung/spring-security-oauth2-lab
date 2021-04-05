package com.test.authorizationserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.authorizationserver.model.dto.UserDto;
import com.test.authorizationserver.model.entity.User;
import com.test.authorizationserver.model.userdetail.JpaUserDetail;
import com.test.authorizationserver.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JpaUserDetailsServiceTest {

    @Autowired
    private JpaUserDetailsServiceImpl jpaUserDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:testUser.json")
    private Resource testUserJson;

    @BeforeEach
    public void init() throws Exception {
        if(this.userRepository.findByUsername("testUser") == null) {
            UserDto testUser = this.objectMapper.readValue(this.testUserJson.getInputStream(),
                    UserDto.class);
            this.userService.persist(testUser);
        }
    }

    @AfterEach
    public void cleanUp() throws Exception {
        User user = this.userRepository.findByUsername("testUser");
        this.userRepository.delete(user);
    }

    @Test
    public void testLoadUserByUsernameSuccess() throws Exception {
        //act
        UserDetails testUser = this.jpaUserDetailsService.loadUserByUsername("testUser");

        //assert
        assertNotNull(testUser);
        assertTrue(testUser instanceof JpaUserDetail);

        JpaUserDetail jpaUserDetail = (JpaUserDetail) testUser;
        Collection<GrantedAuthority> authorities = jpaUserDetail.getAuthorities();
        assertEquals(1, authorities.size());

        GrantedAuthority authority = authorities.stream().collect(Collectors.toList()).get(0);
        assertEquals("read", authority.getAuthority());
    }
}

package com.test.authorizationserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.authorizationserver.model.dto.UserDto;
import com.test.authorizationserver.model.entity.User;
import com.test.authorizationserver.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:testUser.json")
    private Resource testUserJson;

    @AfterEach
    public void cleanUp() throws Exception {
        User user = this.userRepository.findByUsername("testUser");
        this.userRepository.delete(user);
    }

    @Test
    public void testPersistAndFindAllSuccess() throws Exception {
        //arrange
        UserDto testUser = this.objectMapper.readValue(this.testUserJson.getInputStream(),
                UserDto.class);

        //act
        this.userService.persist(testUser);
        List<UserDto> userDtoList = this.userService.findAll();

        //assert
        assertNotNull(userDtoList);
        assertTrue(userDtoList.size() > 0);

        testUser = userDtoList.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase("testUser"))
                .findFirst()
                .orElse(null);
        assertNotNull(testUser);

        List<String> authorityList = testUser.getAuthorities();
        assertNotNull(authorityList);
        assertEquals("read", authorityList.get(0));
    }
}

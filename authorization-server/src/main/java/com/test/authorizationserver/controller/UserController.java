package com.test.authorizationserver.controller;

import com.test.authorizationserver.model.dto.UserDto;
import com.test.authorizationserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//not going to create controller advice for now...

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping(value="/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto persistUser(@Valid @RequestBody UserDto userDto) {
        return this.userService.persist(userDto);
    }

    @GetMapping(value="/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> findAll() {
        return this.userService.findAll();
    }
}

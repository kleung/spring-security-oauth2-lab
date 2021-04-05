package com.test.authorizationserver.service;

import com.test.authorizationserver.mapper.UserDtoMapper;
import com.test.authorizationserver.model.dto.UserDto;
import com.test.authorizationserver.model.entity.Authority;
import com.test.authorizationserver.model.entity.User;
import com.test.authorizationserver.repository.AuthorityRepository;
import com.test.authorizationserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private AuthorityRepository authorityRepository;

    private UserDtoMapper userDtoMapper;

    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto persist(UserDto userDto) {
        User user = this.userDtoMapper.mapUserDto(userDto);
        String plainTextPassword = user.getPassword();
        user.setPassword(this.passwordEncoder.encode(plainTextPassword));

        user = this.userRepository.save(user);

        return this.userDtoMapper.mapUser(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> userList = this.userRepository.findAll();

        return this.userDtoMapper.mapUserList(userList);
    }
}

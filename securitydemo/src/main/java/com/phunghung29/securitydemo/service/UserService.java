package com.phunghung29.securitydemo.service;

import com.phunghung29.securitydemo.dto.*;
import com.phunghung29.securitydemo.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    User findById(Long id);

    List<UserDto> findAll();

    LoginDto login(LoginRequestDto loginRequestDto) throws RuntimeException;

    ResponseEntity<ResponeObject> register(RegisterRequestDto registerRequestDto);


}

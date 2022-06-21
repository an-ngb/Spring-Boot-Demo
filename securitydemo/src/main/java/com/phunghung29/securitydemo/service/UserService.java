package com.phunghung29.securitydemo.service;

import com.phunghung29.securitydemo.dto.*;
import com.phunghung29.securitydemo.entity.Role;
import com.phunghung29.securitydemo.entity.User;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User findById(Long id);

    List<UserDto> findAll();

    LoginDto login(LoginRequestDto loginRequestDto) throws RuntimeException;

    ResponseEntity<ResponeObject> register(RegisterRequestDto registerRequestDto);

    ResponseEntity<ResponeObject> userPermissionChange(ChangeRoleRequestDto changeRoleRequestDto);
}

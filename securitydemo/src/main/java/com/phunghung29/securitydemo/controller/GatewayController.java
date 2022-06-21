package com.phunghung29.securitydemo.controller;

import com.phunghung29.securitydemo.dto.*;
import com.phunghung29.securitydemo.entity.Role;
import com.phunghung29.securitydemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GatewayController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            LoginDto loginDto = userService.login(loginRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponeObject("200", "User login successfully", loginDto));
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("message", e.getMessage());
            return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(err);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponeObject> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return userService.register(registerRequestDto);
    }

    @PutMapping ("/users/userpermissionchange")
    public ResponseEntity<ResponeObject>userPermissionChange(@RequestBody ChangeRoleRequestDto changeRoleRequestDto){
        return userService.userPermissionChange(changeRoleRequestDto);
    }

    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserDto> userDtoList = userService.findAll();
        return ResponseEntity.ok(userDtoList);
    }

}

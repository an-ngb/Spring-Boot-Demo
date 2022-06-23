package com.phunghung29.securitydemo.controller;

import com.phunghung29.securitydemo.dto.*;
import com.phunghung29.securitydemo.entity.Role;
import com.phunghung29.securitydemo.entity.User;
import com.phunghung29.securitydemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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

    @PutMapping("/admin/userpermissionchange")
    public ResponseEntity<?> userPermissionChange(@RequestBody ChangeRoleRequestDto changeRoleRequestDto) {
        return userService.userPermissionChange(changeRoleRequestDto);
    }

    @PutMapping("/admin/userpasswordchange")
    public ResponseEntity<?> userPasswordChange(@RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        return userService.userPasswordChange(changePasswordRequestDto);
    }

    @PostMapping("/search2")
    public ResponseEntity<?> userSearch2(@RequestBody SearchDto searchDto){
        List<UserDto> searchDtoList = userService.userSearch2(searchDto);
        return ResponseEntity.ok(searchDtoList);
    }

    @PostMapping("/search")
    public ResponseEntity<?> userSearch(SearchDto searchDto) {
        List<UserDto> searchDtoList = userService.userSearch(searchDto);
        return ResponseEntity.ok(searchDtoList);
//        if(searchDto.getEmail() == null && searchDto.getRole() == null){
//            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
//        } else if (searchDto.getEmail() == null || searchDto.getRole() == null) {
//            if (searchDto.getEmail().length() > 0){
//                List<UserDto> userDtoList = userService.searchForEmail(searchDto.getEmail());
//                return ResponseEntity.ok(userDtoList);
//            } else{
//                List<UserDto> userDtoList = userService.searchForRole(searchDto.getRole());
//                return ResponseEntity.ok(userDtoList);
//            }
//        } else{
//            List<UserDto> searchDtoList = userService.searchForEmail(searchDto.getEmail());

//            Map mp = new HashMap();
//            mp.put("emailsearch", searchDto.getEmail());
//            mp.put("rolesearch", searchDto.getRole());

//            Map mp = new HashMap();
//            mp.put("emailsearch", searchDtoList);
//            mp.put("rolesearch", searchDtoListByRole);
    }

    //        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponeObject("400", "Search can not be done.", ""));
//    }
//    @PostMapping("/emailsearch")
//    public ResponseEntity<?> searchForEmail(@Param("email") String email) {
//        List<UserDto> userDtoList = userService.searchForEmail(email);
//        return ResponseEntity.ok(userDtoList);
//    }
//    @PostMapping("/rolesearch")
//    public ResponseEntity<?>searchForRole(@Param("role") String role){
//        List<UserDto> userDtoList = userService.searchForRole(role);
//        return ResponseEntity.ok(userDtoList);
//    }

    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserDto> userDtoList = userService.findAll();
        return ResponseEntity.ok(userDtoList);
    }

}

package com.phunghung29.securitydemo.controller;

import com.phunghung29.securitydemo.dto.*;
import com.phunghung29.securitydemo.entity.Product;
import com.phunghung29.securitydemo.entity.Role;
import com.phunghung29.securitydemo.entity.User;
import com.phunghung29.securitydemo.exception.NotEnoughFieldException;
import com.phunghung29.securitydemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
                    new ResponeObject("200", "User login successfully", Instant.now(), loginDto));
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

    @PostMapping("/admin/addproduct")
    public ResponseEntity<?> addProduct(@RequestBody AddProductRequestDto addProductRequestDto) {
        try {
            Product product = userService.addProduct(addProductRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponeObject("200", "Product added successfully.", Instant.now(), addProductRequestDto.getProductName()));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(error);
        }
    }

    @PutMapping("/users/forgotpassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) {
        return userService.forgotPassword(forgotPasswordRequestDto);
    }

    @PostMapping("/search2")
    public ResponseEntity<?> userSearch2(@RequestBody SearchDto searchDto) {
        List<UserDto> searchDtoList = userService.userSearch2(searchDto);
        if (searchDto.getRole() == null || searchDto.getRole().isEmpty() && searchDto.getEmail() == null || searchDto.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponeObject("400", "Search can not be done", Instant.now(), ""));
        } else
            return ResponseEntity.status(HttpStatus.OK).body(new ResponeObject("200", "Search successfully", Instant.now(), searchDtoList));
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

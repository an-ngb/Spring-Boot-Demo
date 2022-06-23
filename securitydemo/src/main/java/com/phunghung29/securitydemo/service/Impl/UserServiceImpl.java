package com.phunghung29.securitydemo.service.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.phunghung29.securitydemo.dto.*;
import com.phunghung29.securitydemo.entity.Role;
import com.phunghung29.securitydemo.entity.User;
import com.phunghung29.securitydemo.repository.RoleRepository;
import com.phunghung29.securitydemo.repository.UserRepository;
import com.phunghung29.securitydemo.service.UserService;
import com.phunghung29.securitydemo.specs.UserSpecs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailService;

    private final Instant instant = Instant.now();

    private final long timeStampMillis = instant.toEpochMilli();


    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            userDto.setRoleName(user.getRole().getRoleName());
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public LoginDto login(LoginRequestDto loginRequestDto) throws RuntimeException {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        try {
            if (authenticate(email, password)) {
                UserDetails userDetails = userDetailService.loadUserByUsername(email);
                User detectedUser = userRepository.findByEmail(email);
                Map<String, Object> payload = new HashMap<>();
                payload.put("id", detectedUser.getId());
                payload.put("email", detectedUser.getEmail());
                payload.put("role", detectedUser.getRole().getRoleName());
                String token = generateToken(payload, new org.springframework.security.core.userdetails.User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()));
                return new LoginDto(token);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("LOGIN_FAILURE");
        }
    }

    @Override
    public ResponseEntity<ResponeObject> register(RegisterRequestDto registerRequestDto) {
        String emailAddress = registerRequestDto.getEmail();
        boolean validEmailAddress = (EmailValidator.getInstance().isValid(emailAddress));
        if (validEmailAddress) {
            User foundUser = userRepository.findByEmail(registerRequestDto.getEmail().trim());
            if (foundUser != null) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponeObject("fail", "User exists.", "")
                );
            }
            Role role = roleRepository.findById(registerRequestDto.getRole_id()).orElse(null);
            if (role == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponeObject("fail", "Role cant not be NULL.", "")
                );
            }
            User user = new User();

            user.setEmail(registerRequestDto.getEmail());

            String passwordValidate = registerRequestDto.getPassword();

            if (passwordValidate == null || passwordValidate.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                        new ResponeObject("400", "Password can not be null.", "")
                );
            } else if (passwordValidate.length() < 8) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                        new ResponeObject("400", "Password must longer than 7 characters.", "")
                );
            } else if (!passwordValidate.matches(".*(?=.*[!@#$%^&*()]).*")) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                        new ResponeObject("400", "Password must contain 1 special character.", "")
                );
            } else {
                String encodedPassword = passwordEncoder.encode(registerRequestDto.getPassword());

                user.setPassword(encodedPassword);

                user.setRole(role);

                userRepository.save(user);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponeObject("200", "User registration successfully.", user.getEmail())
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                new ResponeObject("400", "Email is not valid,  please try again.", ""));
    }

    public static String generateToken
            (Map<String, Object> payload, org.springframework.security.core.userdetails.User user) {
        Properties prop = loadProperties("jwt.setting.properties");
        assert prop != null;
        String key = prop.getProperty("key");
        String accessExpired = prop.getProperty("access_expired");
        assert key != null;
        assert accessExpired != null;
        long expiredIn = Long.parseLong(accessExpired);
        Algorithm algorithm = Algorithm.HMAC256(key);

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiredIn))
                .withClaim("user", payload)
                .sign(algorithm);
    }

    @Override
    public ResponseEntity<?> userPermissionChange(ChangeRoleRequestDto changeRoleRequestDto) {
        Long role = changeRoleRequestDto.getRoleId();
        if (role == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ResponeObject("400", "Role can not be null.", role)
                    );
        } else {
            User foundUser = userRepository.findByEmail(changeRoleRequestDto.getEmail());
            foundUser.setRole(roleRepository.findRoleById(role));
            userRepository.save(foundUser);

            HashMap<String, Object> hmap = new HashMap<String, Object>();
            hmap.put("timestamp", timeStampMillis);
            hmap.put("data", changeRoleRequestDto);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponeObject("200", "Role has been changed", hmap)
            );
        }
    }

    public ResponseEntity<?> userPasswordChange(ChangePasswordRequestDto changePasswordRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentRequestEmail = authentication.getName();
        String newPassword = changePasswordRequestDto.getNewPassword();
        User foundUser = userRepository.findByEmail(changePasswordRequestDto.getEmail());
        if (currentRequestEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponeObject("400", "Email can not be null", ""));
        } else if (newPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponeObject("400", "Password can not be null", ""));
        } else {
            String encodedPassword = passwordEncoder.encode(changePasswordRequestDto.getNewPassword());
            foundUser.setPassword(encodedPassword);
            userRepository.save(foundUser);
        }

        HashMap<String, Object> hmap = new HashMap<String, Object>();
        hmap.put("timestamp", timeStampMillis);
        hmap.put("data", changePasswordRequestDto.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(new ResponeObject("200", "Password has been changed.", hmap));
    }

//    @Override
//    public List<UserDto> searchForEmail(String email) {
//        List<User> userList = userRepository.findByCaseSensitiveEmail(email);
//        if (userList == null || userList.isEmpty()) {
//            throw new RuntimeException("NOT FOUND");
//        }
//        List<UserDto> userDtoList = new ArrayList<>();
//        userList.forEach(item -> {
//            UserDto userDto = new UserDto();
//            BeanUtils.copyProperties(item, userDto);
//            userDto.setRoleName(item.getRole().getRoleName());
//            userDtoList.add(userDto);
//        });
//        return userDtoList;
//    }

    //    @Override
//    public List<UserDto>searchForRole(String role){
//        List<User> userListByRole = userRepository.findByUserRole(role);
//        if (userListByRole == null || userListByRole.isEmpty()) {
//            throw new RuntimeException("NOT FOUND");
//        }
//        List<UserDto> userDtoList = new ArrayList<>();
//        userListByRole.forEach(item -> {
//            UserDto userDto = new UserDto();
//            BeanUtils.copyProperties(item, userDto);
//            userDto.setRoleName(item.getRole().getRoleName());
//            userDtoList.add(userDto);
//        });
//        return userDtoList;
//    }
    @Override
    public List<UserDto> userSearch(SearchDto searchDto) {
        List<User> userListByRoleAndEmail;
         if (searchDto.getRole() != null && searchDto.getEmail() != null){
             userListByRoleAndEmail = userRepository.findByUserRoleAndEmail(searchDto.getRole(), searchDto.getEmail());
         }else if (searchDto.getRole() == null){
             userListByRoleAndEmail = userRepository.findByUserEmail(searchDto.getEmail());
         } else if (searchDto.getEmail() == null) {
             userListByRoleAndEmail = userRepository.findByUserRole(searchDto.getRole());
         } else throw new RuntimeException("NOT FOUND");
//        if (userListByRoleAndEmail == null || userListByRoleAndEmail.isEmpty()) {
//        }
        List<UserDto> userDtoList = new ArrayList<>();
        userListByRoleAndEmail.forEach(item -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(item, userDto);
            userDto.setRoleName(item.getRole().getRoleName());
            userDtoList.add(userDto);
        });
        return userDtoList;
    }

    @Override
    public List<UserDto> userSearch2(SearchDto searchDto) {
        List<User> user = userRepository.findAll(UserSpecs.filter(searchDto));
        List<UserDto> userDtoList = new ArrayList<>();
        user.forEach( item -> {
                    UserDto userDto = new UserDto();
                    BeanUtils.copyProperties(item, userDto);
                    userDto.setRoleName(item.getRole().getRoleName());
                    userDtoList.add(userDto);
                }
        );
        return userDtoList;
    }

    public boolean authenticate(String email, String password) throws Exception {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return true;
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INCORRECT_EMAIL_OR_PASSWORD", e);
        }
    }

    public static Properties loadProperties(String fileName) {
        try (InputStream input = User.class.getClassLoader().getResourceAsStream(fileName)) {

            Properties prop = new Properties();

            if (input == null) {
                throw new IOException();
            }
            prop.load(input);
            return prop;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

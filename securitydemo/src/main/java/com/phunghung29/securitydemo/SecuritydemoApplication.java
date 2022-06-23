package com.phunghung29.securitydemo;

import com.phunghung29.securitydemo.entity.Role;
import com.phunghung29.securitydemo.entity.User;
import com.phunghung29.securitydemo.repository.RoleRepository;
import com.phunghung29.securitydemo.repository.UserRepository;
import com.phunghung29.securitydemo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecuritydemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecuritydemoApplication.class, args);
    }

}

package com.phunghung29.securitydemo.repository;

import com.phunghung29.securitydemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

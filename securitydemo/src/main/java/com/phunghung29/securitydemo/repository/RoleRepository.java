package com.phunghung29.securitydemo.repository;

import com.phunghung29.securitydemo.entity.Role;
import com.phunghung29.securitydemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(Long Role);
}

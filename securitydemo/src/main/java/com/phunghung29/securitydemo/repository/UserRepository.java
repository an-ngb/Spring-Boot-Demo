package com.phunghung29.securitydemo.repository;

import com.phunghung29.securitydemo.dto.SearchDto;
import com.phunghung29.securitydemo.entity.Role;
import com.phunghung29.securitydemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    @Query("select u from User u where u.role.roleName like %:role% and u.email like CONCAT('%', :email, '%')")
    List<User> findByUserRoleAndEmail(@Param("role") String role, @Param("email") String email);

    @Query("select u from User u where u.role.roleName like %:role%")
    List<User> findByUserRole(@Param("role") String role);

    @Query("SELECT u from User u where lower(u.email) like CONCAT('%', :email, '%') ")
    List<User> findByUserEmail(@Param("email") String email);

}

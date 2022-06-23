package com.phunghung29.securitydemo.specs;

import com.phunghung29.securitydemo.dto.SearchDto;
import com.phunghung29.securitydemo.entity.Role;
import com.phunghung29.securitydemo.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import javax.persistence.criteria.*;


public class UserSpecs {
    public static Specification<User> filter(SearchDto searchDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            Join<User, Role> join = root.join("role");
            if (searchDto.getEmail() != null && searchDto.getRole() != null) {
                Predicate emailAndRoleNamePredicate = criteriaBuilder.and(
                        criteriaBuilder.like(criteriaBuilder.lower(join.get("email")), searchDto.getEmail()),
                        criteriaBuilder.like(criteriaBuilder.lower(join.get("role")), searchDto.getRole())
                );
                predicateList.add(emailAndRoleNamePredicate);
            }
            ;
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
//                Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchDto.getEmail());
//                Predicate roleNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("role")), searchDto.getRole());


//                predicateList.addAll(Arrays.asList(emailPredicate, roleNamePredicate));
//            } else if (searchDto.getRole() != null){
//                Predicate roleNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("role")), "%" + searchDto.getRole() + "%");
//                predicateList.add(roleNamePredicate);
//            }
//            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));


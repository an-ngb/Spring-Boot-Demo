package com.phunghung29.securitydemo.repository;

import com.phunghung29.securitydemo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long Product);

    Product findProductByProductName(String name);
}

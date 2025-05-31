package com.my_app.learning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.my_app.learning.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
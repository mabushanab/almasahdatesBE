package com.example.repository;

import com.example.model.Goods;
import com.example.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, Long> {
//    boolean existsByName(String name);
//    Item findByName(String name);
}

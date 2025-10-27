package com.example.repository;

import com.example.dto.ProductDto;
import com.example.model.Goods;
import com.example.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findByItemId(long itemid);
//    boolean existsByName(String name);
//    Item findByName(String name);
}

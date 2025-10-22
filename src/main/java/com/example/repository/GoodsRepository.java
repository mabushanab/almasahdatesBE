package com.example.repository;

import com.example.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
//    Goods findByName(String name);
//    boolean existsByName(String name);
//    Item findByName(String name);
}

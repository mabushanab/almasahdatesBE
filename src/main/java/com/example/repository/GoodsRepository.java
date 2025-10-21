package com.example.repository;

import com.example.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
//    boolean existsByName(String name);
//    Item findByName(String name);
}

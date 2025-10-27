package com.example.repository;

import com.example.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findByItemId(Long itemid);
//    Goods findByName(String name);
//    boolean existsByName(String name);
//    Item findByName(String name);
}

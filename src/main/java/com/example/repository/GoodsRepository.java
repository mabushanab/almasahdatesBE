package com.example.repository;

import com.example.model.Goods;
import com.example.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findByItemId(Long itemid);
    List<Goods> findAllByItemId(Long id);

//    Goods findByName(String name);
//    boolean existsByName(String name);
//    Item findByName(String name);
}

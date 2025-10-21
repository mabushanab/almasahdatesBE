package com.example.repository;

import com.example.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByName(String name);
    Item findByName(String name);
    void deleteByName(String name);
}

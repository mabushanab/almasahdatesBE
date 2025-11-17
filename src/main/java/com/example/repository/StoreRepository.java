package com.example.repository;

import com.example.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByItemName(String name);

    Store findByItemName(String name);
}

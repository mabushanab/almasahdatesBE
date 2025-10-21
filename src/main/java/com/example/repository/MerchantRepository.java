package com.example.repository;

import com.example.model.Customer;
import com.example.model.Item;
import com.example.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Merchant findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);
//    boolean existsByName(String name);
//    Item findByName(String name);
}

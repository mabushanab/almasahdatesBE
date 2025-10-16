package com.example.repository;

import com.example.model.Customer;
import com.example.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
//    boolean existsByName(String name);
//    Item findByName(String name);
}

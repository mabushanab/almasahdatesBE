package com.example.repository;

import com.example.model.Customer;
import com.example.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);
//    boolean existsByName(String name);
//    Item findByName(String name);
}

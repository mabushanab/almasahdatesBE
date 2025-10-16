package com.example.repository;

import com.example.model.Customer;
import com.example.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
//    boolean existsByName(String name);
//    Item findByName(String name);
}

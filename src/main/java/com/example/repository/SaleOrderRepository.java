package com.example.repository;

import com.example.model.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {
    List<SaleOrder> getByCustomerId(Long id);
//    boolean existsByName(String name);
//    Item findByName(String name);
}

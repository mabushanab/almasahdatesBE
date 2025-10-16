package com.example.repository;

import com.example.model.PurchaseOrder;
import com.example.model.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {
//    boolean existsByName(String name);
//    Item findByName(String name);
}

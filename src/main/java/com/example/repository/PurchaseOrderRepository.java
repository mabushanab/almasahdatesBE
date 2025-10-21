package com.example.repository;

import com.example.model.Products;
import com.example.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
//    boolean existsByName(String name);
//    Item findByName(String name);
}

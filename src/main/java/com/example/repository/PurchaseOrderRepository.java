package com.example.repository;

import com.example.model.Products;
import com.example.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
//    PurchaseOrder findByName(String name);
//    boolean existsByName(String name);

//    void deleteByName(String );
//    Item findByName(String name);
}

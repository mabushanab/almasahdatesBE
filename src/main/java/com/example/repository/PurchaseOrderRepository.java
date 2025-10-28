package com.example.repository;

import com.example.model.Products;
import com.example.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> getByMerchantId(Long id);
//    boolean existsByName(String name);

//    void deleteByName(String );
//    Item findByName(String name);
}

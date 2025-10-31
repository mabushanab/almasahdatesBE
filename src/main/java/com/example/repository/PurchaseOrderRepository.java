package com.example.repository;

import com.example.model.Products;
import com.example.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> getByMerchantId(Long id);

//    String findLastPoIdLike(@Param("prefix") String prefix);
//    boolean existsByName(String name);

//    void deleteByName(String );
//    Item findByName(String name);
}

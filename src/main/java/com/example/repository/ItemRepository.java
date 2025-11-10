package com.example.repository;

import com.example.config.TenantContext;
import com.example.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    String tenantId = TenantContext.getTenantId();
    boolean existsByName(String name);
    Item findByName(String name);
    Item findAllByTenantIdAndName(String name,String tenantId);
    List<Item> findAllByTenantId(String tenantId);
    void deleteByName(String name);
}

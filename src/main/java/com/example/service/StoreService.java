package com.example.service;

import com.example.dto.UserDto;
import com.example.exception.UserNotFoundException;
import com.example.model.Goods;
import com.example.model.Products;
import com.example.model.Store;
import com.example.model.User;
import com.example.repository.StoreRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final TenantServiceHelper tenantHelper;
    private final StoreRepository storeRepository;

    public void addToStore(List<Goods> goods) {
        tenantHelper.enableTenantFilter();
//        Store store = new Store();
        goods.forEach(g -> {
            if (storeRepository.existsByItemName(g.getItem().getName())) {
                Store store = storeRepository.findByItemName(g.getItem().getName());
                store.setNumber(store.getNumber() + g.getWeightInGrams());
                storeRepository.save(store);
            } else {
                Store store = new Store();
                store.setItemName(g.getItem().getName());
                store.setNumber(g.getWeightInGrams());
                storeRepository.save(store);
            }

        });
    }

    public void removeFromStore(List<Products> products) {
        tenantHelper.enableTenantFilter();
//        Store store = new Store();
        products.forEach(g -> {
            if (storeRepository.existsByItemName(g.getItem().getName())) {
                Store store = storeRepository.findByItemName(g.getItem().getName());
                store.setNumber(store.getNumber() - g.getQuantity());
                storeRepository.save(store);
            } else {
                Store store = new Store();
                store.setItemName(g.getItem().getName());
                store.setNumber(-g.getQuantity());
                storeRepository.save(store);
            }

        });
    }
}

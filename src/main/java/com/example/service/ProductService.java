package com.example.service;

import com.example.model.Products;
import com.example.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductsRepository productsRepository;
    private final ItemService itemService;


    public List<Products> getAllByItemId(long Itemid) {

        return productsRepository.findByItemId(Itemid);
    }

    public final double getMaxValue(String name) {

       return productsRepository.findAllByItemId(itemService.getEntityByName(name).getId())
                .stream().mapToDouble(Products::getPriceForItem).max().orElse(0.0);
    }
}
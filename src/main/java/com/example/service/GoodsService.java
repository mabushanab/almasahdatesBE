package com.example.service;

import com.example.model.Goods;
import com.example.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepository;
    private final ItemService itemService;
    private final TenantServiceHelper tenantHelper;

    public List<Goods> getAllByItemId(Long Itemid) {
        tenantHelper.enableTenantFilter();
        return goodsRepository.findByItemId(Itemid);
    }

    public final double getMinValue(String name) {
        tenantHelper.enableTenantFilter();
        return goodsRepository.findAllByItemId(itemService.getEntityByName(name).getId())
                .stream().mapToDouble(Goods::getPriceForGrams).min().orElse(0.0);
    }
}
package com.example.service;

//import com.example.dto.CreditDebitDto;

import com.example.dto.*;
import com.example.model.Goods;
import com.example.model.Item;
import com.example.model.Products;
import com.example.model.PurchaseOrder;
import com.example.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    //    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MerchantService merchantService;
    private final ItemService itemService;
    private final PurchaseOrderService purchaseOrderService;
    private final SaleOrderService saleOrderService;
    private final ProductService productService;
    private final GoodsService goodsService;

    public HomeDto getCreditDebitDetails() {
        double poSum = purchaseOrderService.getAllPurchaseOrders().stream().mapToDouble(PurchaseOrderDto::getTotalPrice).sum();
        double poSumRemain = purchaseOrderService.getAllPurchaseOrders().stream().mapToDouble(PurchaseOrderDto::getRemainAmount).sum();
        double soSum = saleOrderService.getAllSaleOrders().stream().mapToDouble(SaleOrderDto::getTotalPrice).sum();
        double soSumRemain = saleOrderService.getAllSaleOrders().stream().mapToDouble(SaleOrderDto::getRemainAmount).sum();



        Map<String, Double> avgProductPrice = itemService.getAllItemsEntities()
                .stream()
                .collect(Collectors.toMap(
                        Item::getName,
                        item -> {
                            List<Products> products = productService.getAllByItemId(item.getId());
                            return products.isEmpty()
                                    ? 0.0
                                    : products.stream()
                                    .mapToDouble(Products::getPriceForItem)
                                    .average()
                                    .orElse(0.0);
                        }
                ));
        Map<String, Double> avgGoodPrice = itemService.getAllItemsEntities()
                .stream()
                .collect(Collectors.toMap(
                        Item::getName,
                        item -> {
                            List<Goods> goods = goodsService.getAllByItemId(item.getId());
                            return goods.isEmpty()
                                    ? 0.0
                                    : goods.stream()
                                    .mapToDouble(Goods::getPriceForGrams)
                                    .average()
                                    .orElse(0.0);
                        }
                ));


        return new HomeDto(poSum, poSumRemain, soSum, soSumRemain, avgProductPrice, avgGoodPrice);

    }
//    public HomeDto getCreditDebitDetails() {
//        double poSum = purchaseOrderService.getAllPurchaseOrders().stream().mapToDouble(PurchaseOrderDto::getRemainAmount).sum();
//        double soSum = saleOrderService.getAllSaleOrders().stream().mapToDouble(SaleOrderDto::getRemainAmount).sum();
//
//        return new HomeDto(poSum, soSum);
//
//    }
}
package com.example.service;

//import com.example.dto.CreditDebitDto;

import com.example.dto.HomeDto;
import com.example.dto.PurchaseOrderDto;
import com.example.dto.SaleOrderDto;
import com.example.model.Goods;
import com.example.model.Item;
import com.example.model.Products;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                            if (products.isEmpty()) {
                                return 0.0;
                            }

                            double avg = products.stream()
                                    .mapToDouble(Products::getPriceForItem)
                                    .average()
                                    .orElse(0.0);

                            // Round to 2 decimals
                            return Math.round(avg * 100.0) / 100.0;
                        }
                ));
        Map<String, Double> avgProductPriceWithBox = itemService.getAllItemsEntities()
                .stream()
                .collect(Collectors.toMap(
                        Item::getName,
                        item -> {
                            List<Products> products = productService.getAllByItemId(item.getId());
                            if (products.isEmpty()) {
                                return 0.0;
                            }

                            double avg = products.stream()
                                    .mapToDouble(p -> p.getPriceForItem() + p.getBoxCost())
                                    .average()
                                    .orElse(0.0);

                            return Math.round(avg * 100.0) / 100.0;
                        }
                ));

        Map<String, Double> avgGoodPrice = itemService.getAllItemsEntities()
                .stream()
                .collect(Collectors.toMap(
                        Item::getName,
                        item -> {
                            List<Goods> goods = goodsService.getAllByItemId(item.getId());
                            if (goods.isEmpty()) {
                                return 0.0;
                            }

                            double avg = goods.stream()
                                    .mapToDouble(Goods::getPriceForGrams)
                                    .average()
                                    .orElse(0.0);

                            return Math.round(avg * 100.0) / 100.0;
                        }
                ));

        Map<String, Double> avgProfitPerItem = avgProductPrice.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            String itemName = entry.getKey();
                            double productAvg = entry.getValue();
                            double goodAvg = avgGoodPrice.getOrDefault(itemName, 0.0);

                            double diff = productAvg - goodAvg;

                            // Round to 2 decimals
                            return Math.round(diff * 100.0) / 100.0;
                        }
                ));

        Map<String, Double> totalProfitPerItem = itemService.getAllItemsEntities()
                .stream()
                .collect(Collectors.toMap(
                        Item::getName,
                        item -> {
                            List<Products> products = productService.getAllByItemId(item.getId());
                            List<Goods> goods = goodsService.getAllByItemId(item.getId());

                            if (products.isEmpty() || goods.isEmpty()) {
                                return 0.0;
                            }

                            double avgProductPricePerItem = products.stream()
                                    .mapToDouble(Products::getPriceForItem)
                                    .average()
                                    .orElse(0.0);

                            double totalQuantity = products.stream()
                                    .mapToDouble(Products::getQuantity)
                                    .sum();

                            double totalProfit = avgProductPricePerItem * totalQuantity;

                            return Math.round(totalProfit * 100.0) / 100.0;
                        }
                ));




        return new HomeDto(poSum, poSumRemain, soSum, soSumRemain, avgProductPrice, avgGoodPrice
                , avgProductPriceWithBox, avgProfitPerItem, totalProfitPerItem);

    }


}
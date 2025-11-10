package com.example.service;

//import com.example.dto.CreditDebitDto;

import com.example.dto.HomeDto;
import com.example.dto.PurchaseOrderDto;
import com.example.dto.SaleOrderDto;
import com.example.model.*;
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
    private final CustomerService customerService;
    private final ItemService itemService;
    private final PurchaseOrderService purchaseOrderService;
    private final SaleOrderService saleOrderService;
    private final ProductService productService;
    private final GoodsService goodsService;
    private final TenantServiceHelper tenantHelper;

    public HomeDto getCreditDebitDetails() {
        tenantHelper.enableTenantFilter();
        double poSum = Math.round(purchaseOrderService.getAllPurchaseOrders().stream().mapToDouble(PurchaseOrderDto::getTotalPrice).sum() * 100.00) /100.00;
        double poSumRemain = Math.round(purchaseOrderService.getAllPurchaseOrders().stream().mapToDouble(PurchaseOrderDto::getRemainAmount).sum() * 100.00) /100.00;
        double soSum = Math.round(saleOrderService.getAllSaleOrders().stream().mapToDouble(SaleOrderDto::getTotalPrice).sum() * 100.00) /100.00;
        double soSumRemain = Math.round(saleOrderService.getAllSaleOrders().stream().mapToDouble(SaleOrderDto::getRemainAmount).sum() * 100.00) /100.00;
//        Math.round(totalAmount * 100.00) /100.00

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

//        Map<String, Double> merchantRemainAmount = merchantService.getAllMerchantsEntities()
//                .stream().collect(Collectors.toMap(
//                        Merchant::getName, m -> {
//                            return purchaseOrderService.getByMerchantId(m.getId())
//                                    .stream().mapToDouble(PurchaseOrder::getRemainAmount).sum();
//                        }
//                ));

        Map<String, List<SaleOrder>> customerSOs = customerService.getAllSaleOrdersEntities()
                .stream().collect(Collectors.toMap(
                        Customer::getName, c -> {
                            return saleOrderService.getByCustomerId(c.getId());
                        }
                ));

        Map<String, List<PurchaseOrder>> merchantPOs = merchantService.getAllMerchantsEntities()
                .stream().collect(Collectors.toMap(
                        Merchant::getName, m -> {
                            return purchaseOrderService.getByMerchantId(m.getId());
                        }
                ));
        Map<String, Double> merchantRemainAmount = merchantPOs.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .mapToDouble(PurchaseOrder::getRemainAmount)
                                .sum()
                ));


//
//        Map<String, List<Float>> merchantRemainAmount = merchantService.getAllMerchantsEntities()
//                .stream().collect(Collectors.toMap(
//                        Merchant::getName, m -> {
//                            return purchaseOrderService.getByMerchantId(m.getId())
//                                    .stream().map(PurchaseOrder::getRemainAmount).toList();
//                        }
//                ));


        return new HomeDto(poSum, poSumRemain, soSum, soSumRemain, avgProductPrice, avgGoodPrice
                , avgProductPriceWithBox, avgProfitPerItem, totalProfitPerItem, merchantRemainAmount, merchantPOs, customerSOs);

    }


}
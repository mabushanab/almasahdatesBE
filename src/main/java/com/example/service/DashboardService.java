package com.example.service;

import com.example.dto.dashboard.DashboardDto;
import com.example.dto.PurchaseOrderDto;
import com.example.dto.SaleOrderDto;
import com.example.dto.dashboard.ProfitDashboardDto;
import com.example.dto.dashboard.SumAvgCountPoSoDashboardDto;
import com.example.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MerchantService merchantService;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final PurchaseOrderService purchaseOrderService;
    private final SaleOrderService saleOrderService;
    private final ProductService productService;
    private final GoodsService goodsService;
    private final TenantServiceHelper tenantHelper;

    public DashboardDto getProfitInfo1() {
        tenantHelper.enableTenantFilter();

        Map<String, Double> avgProductPrice = new HashMap<>();
        Map<String, Double> avgProductPriceWithBox = new HashMap<>();
        Map<String, Double> avgGoodPrice = new HashMap<>();
        Map<String, Double> avgProfitPerItem = new HashMap<>();
        Map<String, Double> totalProfitPerItem = new HashMap<>();

        List<Item> items = itemService.getAllItemsEntities();

        Map<String, List<Products>> productsByItem = items
                .stream()
                .collect(Collectors.toMap(Item::getName, item -> productService.getAllByItemId(item.getId())));

        Map<String, List<Goods>> goodsByItem =
                items.stream().collect(Collectors.toMap(
                        Item::getName,
                        item -> goodsService.getAllByItemId(item.getId())
                ));


        for (Item item : items) {
            String name = item.getName();

            List<Products> products = productsByItem.get(name);
            List<Goods> goods = goodsByItem.get(name);

            // Avg product price
            double avgP = avgDouble(products.stream()
                    .map(Products::getPriceForItem)
                    .toList());

            avgProductPrice.put(name, round2(avgP));

            // Avg product price + box
            double avgPBox = avgDouble(products.stream()
                    .map(p -> p.getPriceForItem() + p.getBoxCost())
                    .toList());

            avgProductPriceWithBox.put(name, round2(avgPBox));

            // Avg good price
            double avgG = avgDouble(goods.stream()
                    .map(Goods::getPriceForGrams)
                    .toList());

            avgGoodPrice.put(name, round2(avgG));

            // Avg Profit per Item
            double profit = avgP - avgG;
            avgProfitPerItem.put(name, round2(profit));

            // Total profit = (avg product price - avg goods price) * total quantity
            double totalQuantity = products.stream()
                    .mapToDouble(Products::getQuantity)
                    .sum();

            double totalProfit = profit * totalQuantity;
            totalProfitPerItem.put(name, round2(totalProfit));
        }


//

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

        Map<String, Double> merchantTotalAmount = merchantPOs.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .mapToDouble(PurchaseOrder::getTotalPrice)
                                .sum()
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

        return new DashboardDto(purchaseOrderService.sumPo(purchaseOrderService.getAllPurchaseOrders()),
                purchaseOrderService.sumPoRemain(purchaseOrderService.getAllPurchaseOrders()),
                saleOrderService.sumSo(saleOrderService.getAllSaleOrders()),
                saleOrderService.sumSoRemain(saleOrderService.getAllSaleOrders()), avgProductPrice, avgGoodPrice
                , avgProductPriceWithBox, avgProfitPerItem, totalProfitPerItem, merchantTotalAmount, merchantRemainAmount);

    }

    public ProfitDashboardDto getProfitInfo() {
        tenantHelper.enableTenantFilter();

        List<Products> products = productService.getAll();
        List<Goods> goods = goodsService.getAll();

        double sumProducts = round2(products.stream().mapToDouble(p-> p.getPriceForItem() * p.getQuantity()).sum());
        double sumGoods = round2(goods.stream().mapToDouble(p-> p.getPriceForGrams() * p.getWeightInGrams()).sum());



        Map<String, Double> avgProductPrice = new HashMap<>();
        Map<String, Double> avgProductPriceWithBox = new HashMap<>();
        Map<String, Double> avgGoodPrice = new HashMap<>();
        Map<String, Double> avgProfitPerItem = new HashMap<>();
        Map<String, Double> totalProfitPerItem = new HashMap<>();

        List<Item> items = itemService.getAllItemsEntities();

        Map<String, List<Products>> productsByItem = items
                .stream()
                .collect(Collectors.toMap(Item::getName, item -> productService.getAllByItemId(item.getId())));

        Map<String, List<Goods>> goodsByItem =
                items.stream().collect(Collectors.toMap(
                        Item::getName,
                        item -> goodsService.getAllByItemId(item.getId())
                ));


        for (Item item : items) {
            String name = item.getName();

            List<Products> products = productsByItem.get(name);
            List<Goods> goods = goodsByItem.get(name);

            // Avg product price
            double avgP = avgDouble(products.stream()
                    .map(Products::getPriceForItem)
                    .toList());

            avgProductPrice.put(name, round2(avgP));

            // Avg product price + box
            double avgPBox = avgDouble(products.stream()
                    .map(p -> p.getPriceForItem() + p.getBoxCost())
                    .toList());

            avgProductPriceWithBox.put(name, round2(avgPBox));

                // Avg good price
            double avgG = avgDouble(goods.stream()
                    .map(Goods::getPriceForGrams)
                    .toList());

            avgGoodPrice.put(name, round2(avgG));

            // Avg Profit per Item
            double profit = avgP - avgG;
            avgProfitPerItem.put(name, round2(profit));

            // Total profit = (avg product price - avg goods price) * total quantity
            double totalQuantity = products.stream()
                    .mapToDouble(Products::getQuantity)
                    .sum();

            double totalProfit = profit * totalQuantity;
            totalProfitPerItem.put(name, round2(totalProfit));
        }

        List<PurchaseOrderDto> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
        List<SaleOrderDto> saleOrders = saleOrderService.getAllSaleOrders();

        return new ProfitDashboardDto(sumProducts,sumGoods,sumProducts);

    }

    public SumAvgCountPoSoDashboardDto getPoSoInfo() {
        tenantHelper.enableTenantFilter();
        List<PurchaseOrderDto> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
        List<SaleOrderDto> saleOrders = saleOrderService.getAllSaleOrders();

        return new SumAvgCountPoSoDashboardDto(purchaseOrders.size(), purchaseOrderService.sumPo(purchaseOrders),
                purchaseOrderService.sumPoRemain(purchaseOrders), saleOrders.size(),
                saleOrderService.sumSo(saleOrders),
                saleOrderService.sumSoRemain(saleOrders));

    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private double avgDouble(List<Double> list) {
        return list.isEmpty() ? 0.0 :
                list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
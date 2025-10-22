package com.example.controller;

import com.example.dto.PurchaseOrderDto;
import com.example.model.PurchaseOrder;
import com.example.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchaseOrder")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

//    @GetMapping("/list")
//    public List<PurchaseOrderDto> getAllProducts2() {
//        return purchaseOrderService.getAllPurchaseOrders();
//    }

    @PostMapping("/create")
    public String createProduct(@RequestBody PurchaseOrderDto purchaseOrderDto) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrderDto);
    }


//    @GetMapping("/{name}")
//    public PurchaseOrderDto getUser(@PathVariable String name) {
//        return purchaseOrderService.getByName(name);
//    }

//    @DeleteMapping("/{name}")
//    public String deleteItem(@PathVariable String name) {
//        return purchaseOrderService.deletePurchaseOrder(name);
//    }

//    @PostMapping("/createList")
//    public String createProduct(@RequestBody List<PurchaseOrder> purchaseOrders) {
//        return purchaseOrderService.createPurchaseOrderList(purchaseOrders);
//    }
}

package com.example.controller;

import com.example.dto.SaleOrderDto;
import com.example.service.SaleOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saleOrder")
@RequiredArgsConstructor
public class SaleOrderController {

    private final SaleOrderService saleOrderService;

    @GetMapping("/list")
    public List<SaleOrderDto> getAllProducts2() {
        return saleOrderService.getAllSaleOrders();
    }

    @PostMapping("/create")
    public String createProduct(@RequestBody SaleOrderDto saleOrderDto) {
        return saleOrderService.createSaleOrder(saleOrderDto);
    }


//    @GetMapping("/{name}")
//    public SaleOrderDto getUser(@PathVariable String name) {
//        return saleOrderService.getByName(name);
//    }

//    @DeleteMapping("/{name}")
//    public String deleteItem(@PathVariable String name) {
//        return saleOrderService.deleteSaleOrder(name);
//    }

//    @PostMapping("/createList")
//    public String createProduct(@RequestBody List<SaleOrder> saleOrders) {
//        return saleOrderService.createSaleOrderList(saleOrders);
//    }
}

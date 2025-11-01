package com.example.controller;

import com.example.dto.SaleOrderDto;
import com.example.service.SaleOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
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

    @GetMapping("/productMaxPrice")
    public double getProductMaxPriceByName(@RequestParam String productName) {
        return saleOrderService.getMaxValue(productName);
    }
    @GetMapping("/invoice")
    public ResponseEntity<byte[]> getInvoice(@RequestParam String sOId) {
        byte[] pdf = saleOrderService.generateInvoice2(sOId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("invoice.pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
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

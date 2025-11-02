package com.example.controller;

import com.example.dto.SaleOrderDto;
import com.example.model.SaleOrder;
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

    @GetMapping("/productMinPrice")
    public double getProductMaxPriceByName(@RequestParam String productName) {
        return saleOrderService.getMaxValue(productName);
    }

    @GetMapping("/productPrice")
    public double productPrice(@RequestParam String productName) {
        return saleOrderService.productPrice(productName);
    }

    @GetMapping("/invoice")
    public ResponseEntity<byte[]> getInvoice(@RequestParam String sOId) {
        byte[] pdf = saleOrderService.generateInvoice2(sOId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("invoice.pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/payRemainAmount")
    public String payRemainAmount(@RequestParam String sOId, @RequestParam double amount) {
        return saleOrderService.payRemainAmount(sOId,amount);
    }

    @GetMapping("/payAllRemainAmount")
    public String payAllRemainAmount(@RequestParam String sOId) {
        return saleOrderService.payAllRemainAmount(sOId);
    }
}

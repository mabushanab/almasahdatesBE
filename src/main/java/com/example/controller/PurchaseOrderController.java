package com.example.controller;

import com.example.dto.MerchantDto;
import com.example.dto.PurchaseOrderDto;
import com.example.model.PurchaseOrder;
import com.example.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchaseOrder")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @GetMapping("/list")
    public List<PurchaseOrderDto> getAllProducts2() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @PostMapping("/create")
    public String createProduct(@RequestBody PurchaseOrderDto purchaseOrderDto) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrderDto);
    }

    @GetMapping("/goodsMinPrice")
    public double getProductMinPriceByName(@RequestParam String goodsName) {
        return purchaseOrderService.getMinValue(goodsName);
    }

    @GetMapping("/invoice")
    public ResponseEntity<byte[]> getInvoice(@RequestParam String pOId) {
        byte[] pdf = purchaseOrderService.generateInvoice(pOId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("invoice.pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/payRemainAmount")
    public String payRemainAmount(@RequestParam String pOId, @RequestParam double amount) {
        return purchaseOrderService.payRemainAmount(pOId,amount);
    }
    @GetMapping("/payAllRemainAmount")
    public String payAllRemainAmount(@RequestParam String pOId) {
        return purchaseOrderService.payAllRemainAmount(pOId);
    }
    @GetMapping("/POs")
    public List<PurchaseOrderDto> getMerchantPOs(@RequestParam String merchantName) {
        return purchaseOrderService.getByMerchantName(merchantName);

    }
}

package com.example.service;

import com.example.dto.GoodsDto;
import com.example.dto.PurchaseOrderDto;
import com.example.model.Goods;
import com.example.model.PurchaseOrder;
import com.example.repository.PurchaseOrderRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MerchantService merchantService;
    private final ItemService itemService;
    private final PdfService pdfService;

//    public PurchaseOrderDto getByName(String name) {
//        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByName(name);
//
//           List<Goods> goods =
//        return new PurchaseOrderDto(purchaseOrder.getMerchant(), purchaseOrder.getGoods(),purchaseOrder.getDate(), purchaseOrder.getTotalPrice(),
//                 purchaseOrder.getRemainAmount(), purchaseOrder.getNotes());
//    }

    public List<PurchaseOrderDto> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream().
                map(purchaseOrder ->
                        new PurchaseOrderDto(purchaseOrder.getMerchant().getName(), purchaseOrder.getGoods().stream().map(g -> new GoodsDto(
                                g.getItem().getName(), g.getPriceForGrams(), g.getWeightInGrams(), g.getNotes()
                        )).toList(), purchaseOrder.getDate(), purchaseOrder.getTotalPrice(),
                                purchaseOrder.getRemainAmount(), purchaseOrder.getNotes())).toList();
    }

    public String createPurchaseOrder(PurchaseOrderDto purchaseOrderDto) {

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setMerchant(merchantService.getMerchantByName(purchaseOrderDto.getMerchantName()));
        purchaseOrder.setGoods(purchaseOrderDto.getGoods().stream().map(
                        g -> new Goods(itemService.getEntityByName(g.getItemName()), g.getPriceForGrams(), g.getWeightInGrams(), g.getNotes()))
                .toList());
        purchaseOrder.setDate(LocalDate.now());
        purchaseOrder.setRemainAmount(purchaseOrderDto.getRemainAmount());
        purchaseOrder.setTotalPrice(purchaseOrderDto.getTotalPrice());
        purchaseOrder.setNotes(purchaseOrderDto.getNotes());
        purchaseOrderRepository.save(purchaseOrder);
        return "The Purchase Order for merchant : " + purchaseOrder.getMerchant().getName() + " saved successfully";
    }

    @Transactional
    public String deletePurchaseOrder(Long id) {
        purchaseOrderRepository.deleteById(id);
        return "The PurchaseOrder deleted successfully";
    }

    public List<PurchaseOrder> getByMerchantId(Long id) {
        return purchaseOrderRepository.getByMerchantId(id);
    }

    public byte[] generateInvoice(String customerName, double totalAmount) {
        return pdfService.generateInvoice(customerName, totalAmount, getByMerchantId(
                merchantService.getMerchantByName(customerName).getId()
        ));

    }

    public byte[] generateInvoice2(String customerName, double totalAmount) {

        getByMerchantId(merchantService.getMerchantByName(customerName).getId())
                .stream().collect(Collectors.toMap(
                 po -> po.getTotalPrice(),
                        )
                )


        return pdfService.generateInvoice(customerName, totalAmount, getByMerchantId(
                merchantService.getMerchantByName(customerName).getId()
        ));

    }
}
package com.example.service;

import com.example.dto.GoodsDto;
import com.example.dto.PurchaseOrderDto;
import com.example.model.Goods;
import com.example.model.PurchaseOrder;
import com.example.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MerchantService merchantService;
    private final ItemService itemService;
    private final PdfService pdfService;
    private final JdbcTemplate jdbcTemplate;
    private final GoodsService goodsService;

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
                        new PurchaseOrderDto(purchaseOrder.getPOId(), purchaseOrder.getMerchant().getName(), purchaseOrder.getGoods().stream().map(g -> new GoodsDto(
                                g.getItem().getName(), g.getPriceForGrams(), g.getWeightInGrams(), g.getNotes()
                        )).toList(), purchaseOrder.getDate(), purchaseOrder.getTotalPrice(),
                                purchaseOrder.getRemainAmount(), purchaseOrder.getNotes())).toList();
    }

    @Transactional
    public String createPurchaseOrder(PurchaseOrderDto purchaseOrderDto) {

        long nextVal = getNextSequenceValue("purchase_order");
        String poId = "PO-" + LocalDate.now().toString().replace("-","")
                + "-" + String.format("%04d", nextVal);


        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setPOId(poId);
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

    @Transactional
    public int getNextSequenceValue(String name) {
        // Step 1: Update the value
        jdbcTemplate.update(
                "UPDATE sequence_counter SET next_val = next_val + 1 WHERE name = ?",
                name
        );

        // Step 2: Retrieve the new value
        Integer nextVal = jdbcTemplate.queryForObject(
                "SELECT next_val FROM sequence_counter WHERE name = ?",
                Integer.class,
                name
        );

        return nextVal != null ? nextVal : 1;
    }

    public List<PurchaseOrder> getByMerchantId(Long id) {
        return purchaseOrderRepository.getByMerchantId(id);
    }

    public byte[] generateInvoice(String customerName, double totalAmount) {
        return pdfService.generateInvoice(customerName,Math.round(totalAmount * 100.00) /100.00 , getByMerchantId(
                merchantService.getMerchantByName(customerName).getId()
        ));

    }

    public byte[] generateInvoice2(String sOId) {
        return pdfService.generateInvoicePOs(purchaseOrderRepository.getBypOId(sOId).getMerchant().getName(),
                purchaseOrderRepository.getBypOId(sOId).getGoods());
    }

    public double getMinValue(String name){
        return Math.round(goodsService.getMinValue(name) * 100.00) /100.00;
    }

//    public byte[] generateInvoice2(String customerName, double totalAmount) {
//
//        getByMerchantId(merchantService.getMerchantByName(customerName).getId())
//                .stream().collect(Collectors.toMap(
//                 po -> po.getTotalPrice(),
//                        )
//                )
//
//
//        return pdfService.generateInvoice(customerName, totalAmount, getByMerchantId(
//                merchantService.getMerchantByName(customerName).getId()
//        ));
//
//    }
}
package com.example.service;

import com.example.dto.CustomerDataResponse;
import com.example.dto.GoodsDto;
import com.example.dto.MerchantDataResponse;
import com.example.dto.PurchaseOrderDto;
import com.example.model.Goods;
import com.example.model.Item;
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
    private final StoreService storeService;
    private final TenantServiceHelper tenantHelper;


    public List<PurchaseOrderDto> getAllPurchaseOrders() {
        tenantHelper.enableTenantFilter();
//        System.out.println("pos " + purchaseOrderRepository.findAll().stream()
//                .map(PurchaseOrder::getPOId)
//                .toList());

        return purchaseOrderRepository.findAll().stream().
                map(purchaseOrder ->
                        new PurchaseOrderDto(purchaseOrder.getPOId(), purchaseOrder.getMerchant().getName(), purchaseOrder.getGoods().stream().map(g -> new GoodsDto(
                                g.getItem().getName(), g.getPriceForGrams(), g.getWeightInGrams(), g.getDiscount(), g.getNotes()
                        )).toList(), purchaseOrder.getDate(), purchaseOrder.getTotalPrice(),
                                purchaseOrder.getRemainAmount(), purchaseOrder.getNotes())).toList();
    }

    @Transactional
    public String createPurchaseOrder(PurchaseOrderDto purchaseOrderDto) {
        tenantHelper.enableTenantFilter();


        long nextVal = getNextSequenceValue("purchase_order");
        String poId = "PO-" + LocalDate.now().toString().replace("-", "")
                + "-" + String.format("%04d", nextVal);


        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setPOId(poId);
        purchaseOrder.setMerchant(merchantService.getMerchantByName(purchaseOrderDto.getMerchantName()));
        purchaseOrder.setGoods(purchaseOrderDto.getGoods().stream().map(
                        g -> new Goods(itemService.getEntityByName(g.getItemName()), g.getPriceForGrams(), g.getWeightInGrams(), g.getDiscount(), g.getNotes()))
                .toList());
        purchaseOrder.setDate(LocalDate.now());
        purchaseOrder.setRemainAmount(purchaseOrderDto.getRemainAmount());
        purchaseOrder.setTotalPrice(purchaseOrderDto.getTotalPrice());
        purchaseOrder.setNotes(purchaseOrderDto.getNotes());
        purchaseOrderRepository.save(purchaseOrder);
        storeService.addToStore(purchaseOrder.getGoods().stream().toList());

        return "The Purchase Order for merchant : " + purchaseOrder.getMerchant().getName() + " saved successfully";
    }

    @Transactional
    public String deletePurchaseOrder(Long id) {
        tenantHelper.enableTenantFilter();
        purchaseOrderRepository.deleteById(id);
        return "The PurchaseOrder deleted successfully";
    }

    @Transactional
    public int getNextSequenceValue(String name) {
        tenantHelper.enableTenantFilter();
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
        tenantHelper.enableTenantFilter();
        return purchaseOrderRepository.getByMerchantId(id);
    }

    public MerchantDataResponse getByMerchantName(String name) {
//        tenantHelper.enableTenantFilter();

        List<PurchaseOrderDto> pos = getByMerchantId(merchantService.getMerchantByName(name).getId()).stream().map(
                purchaseOrder -> new PurchaseOrderDto(purchaseOrder.getPOId(),
                        purchaseOrder.getMerchant().getName(),
                        purchaseOrder.getGoods().stream().map(g -> new GoodsDto(
                                g.getItem().getName(), g.getPriceForGrams(), g.getWeightInGrams(), g.getDiscount(), g.getNotes()
                        )).toList()
                        , purchaseOrder.getDate(),
                        purchaseOrder.getTotalPrice(),
                        purchaseOrder.getRemainAmount(), purchaseOrder.getNotes())).toList();

        return new MerchantDataResponse(pos, pos.stream().mapToDouble(PurchaseOrderDto::getTotalPrice).sum(), pos.stream().mapToDouble(PurchaseOrderDto::getRemainAmount).sum());
    }

    public byte[] generateInvoice(String pOId) {
        tenantHelper.enableTenantFilter();
        return pdfService.generateInvoicePOs(purchaseOrderRepository.getBypOId(pOId));
    }

    public double getMinValue(String name) {
        return Math.round(goodsService.getMinValue(name) * 100.00) / 100.00;
    }

    public String payRemainAmount(String pOId, double amount) {
        tenantHelper.enableTenantFilter();
        PurchaseOrder purchaseOrder = purchaseOrderRepository.getBypOId(pOId);
        if (purchaseOrder.getRemainAmount() > amount) {
            purchaseOrder.setRemainAmount((purchaseOrder.getRemainAmount() - amount));
            purchaseOrderRepository.save(purchaseOrder);
            return "The PO: " + pOId + " amount " + amount + "is payed.";
        } else if (purchaseOrder.getRemainAmount() == amount) {
            purchaseOrder.setRemainAmount((purchaseOrder.getRemainAmount() - amount));
            purchaseOrderRepository.save(purchaseOrder);
            return "The PO: " + pOId + " fully amount is payed.";

        } else return "Amount is bigger than Remaining.";
    }

    public String payAllRemainAmount(String pOId) {
        tenantHelper.enableTenantFilter();
        PurchaseOrder purchaseOrder = purchaseOrderRepository.getBypOId(pOId);
        purchaseOrder.setRemainAmount(0);
        purchaseOrderRepository.save(purchaseOrder);
        return "The PO: " + pOId + " amount is fully payed.";
    }
}
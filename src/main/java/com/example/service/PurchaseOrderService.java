package com.example.service;

import com.example.dto.PurchaseOrderDto;
import com.example.model.Goods;
import com.example.model.PurchaseOrder;
import com.example.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderDto getByName(String name) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByName(name);
//        List<Goods> goods =
        return new PurchaseOrderDto(purchaseOrder.getMerchant(), purchaseOrder.getGoods(),purchaseOrder.getDate(), purchaseOrder.getTotalPrice(),
                 purchaseOrder.getRemainAmount(), purchaseOrder.getNotes());
    }

    public List<PurchaseOrderDto> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream().
                map(purchaseOrder -> new PurchaseOrderDto(purchaseOrder.getMerchant(), purchaseOrder.getGoods(),purchaseOrder.getDate(), purchaseOrder.getTotalPrice(),
                        purchaseOrder.getRemainAmount(), purchaseOrder.getNotes())).toList();
    }

    public String createPurchaseOrder(PurchaseOrder purchaseOrder) {
         purchaseOrderRepository.save(purchaseOrder);
        return "The Purchase Order for merchant : " + purchaseOrder.getMerchant().getName() + " saved successfully";
    }

    @Transactional
    public String deletePurchaseOrder(Long id) {
        purchaseOrderRepository.deleteById(id);
        return "The PurchaseOrder deleted successfully";
    }

//    public String createPurchaseOrderList(List<PurchaseOrder> purchaseOrders) {
//        List<PurchaseOrder> p = purchaseOrders.stream().filter(purchaseOrder -> !purchaseOrderRepository.existsByName(purchaseOrder.getName())).toList();
//        if (p.isEmpty())
//            return "All purchaseOrders already exist";
//        else {
//            String S = p.stream().map(m -> m.getName() + ' ').toString();
//            purchaseOrderRepository.saveAll(p);
//            return "The PurchaseOrders: " + S + " saved successfully";
//        }
//
//    }
}
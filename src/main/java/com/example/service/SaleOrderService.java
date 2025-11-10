package com.example.service;

import com.example.dto.*;
import com.example.model.Products;
import com.example.model.SaleOrder;
import com.example.repository.SaleOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleOrderService {

    private final SaleOrderRepository saleOrderRepository;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final PdfService pdfService;
    private final JdbcTemplate jdbcTemplate;
    private final ProductService productService;
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final TenantServiceHelper tenantHelper;

    public List<SaleOrderDto> getAllSaleOrders() {
        tenantHelper.enableTenantFilter();
        return saleOrderRepository.findAll().stream().
                map(saleOrder ->
                        new SaleOrderDto(saleOrder.getSOId(), saleOrder.getCustomer().getName(),
                                saleOrder.getProducts().stream().map(g -> new ProductDto(
                                        g.getItem().getName(), g.getPriceForItem(), g.getQuantity(), g.getBoxCost(), g.getDiscount(), g.getNotes()
                                )).toList(), saleOrder.getDate(), saleOrder.getTotalPrice(),
                                saleOrder.getRemainAmount(), saleOrder.getNotes())).toList();
    }

    public String createSaleOrder(SaleOrderDto saleOrderDto) {
        tenantHelper.enableTenantFilter();
        long nextVal = getNextSequenceValue("sale_order");
        String soId = "SO-" + LocalDate.now().toString().replace("-", "")
                + "-" + String.format("%04d", nextVal);
        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setSOId(soId);
        saleOrder.setCustomer(customerService.getCustomerByName(saleOrderDto.getCustomerName()));
        saleOrder.setProducts(saleOrderDto.getProducts().stream().map(
                        g -> new Products(itemService.getEntityByName(g.getItemName()), g.getPriceForItem(), g.getQuantity(), g.getBoxCost(), g.getDiscount(), g.getNotes()))
                .toList());
        saleOrder.setDate(LocalDate.now());
        saleOrder.setRemainAmount(saleOrderDto.getRemainAmount());
        saleOrder.setTotalPrice(saleOrderDto.getTotalPrice());
        saleOrder.setNotes(saleOrderDto.getNotes());
        saleOrderRepository.save(saleOrder);
        return "The Sale Order for customer : " + saleOrder.getCustomer().getName() + " saved successfully";
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

    @Transactional
    public String deleteSaleOrder(Long id) {
        tenantHelper.enableTenantFilter();
        saleOrderRepository.deleteById(id);
        return "The SaleOrder deleted successfully";
    }

    public List<SaleOrder> getByCustomerId(Long id) {
        tenantHelper.enableTenantFilter();
        return saleOrderRepository.getByCustomerId(id);
    }

    public byte[] generateInvoice(String sOId) {
        tenantHelper.enableTenantFilter();
        return pdfService.generateInvoiceSOs(
                saleOrderRepository.getBysOId(sOId));
    }

    public double getMaxValue(String name) {
        return Math.round(productService.getMaxValue(name) * 100.00) / 100.00;
    }

    public double productPrice(String name) {
        return Math.round(itemService.getSalePrice(name) * 100.00) / 100.00;
    }


    public String payRemainAmount(String sOId, double amount) {
        tenantHelper.enableTenantFilter();
        SaleOrder saleOrder = saleOrderRepository.getBysOId(sOId);
        if (saleOrder.getRemainAmount() > amount) {
            saleOrder.setRemainAmount((saleOrder.getRemainAmount() - amount));
            saleOrderRepository.save(saleOrder);
            return "The SO: " + sOId + " amount " + amount + "is payed.";
        } else if (saleOrder.getRemainAmount() == amount) {
            saleOrder.setRemainAmount((saleOrder.getRemainAmount() - amount));
            saleOrderRepository.save(saleOrder);
            return "The SO: " + sOId + " fully amount is payed.";

        } else return "Amount is bigger than Remaining.";
    }

    public String payAllRemainAmount(String sOId) {
        tenantHelper.enableTenantFilter();
        SaleOrder saleOrder = saleOrderRepository.getBysOId(sOId);
        saleOrder.setRemainAmount(0);
        saleOrderRepository.save(saleOrder);
        return "The SO: " + sOId + " amount is fully payed.";
    }

//    public List<SaleOrderDto> getByCustomerName(String name) {
//        return getByCustomerId(customerService.getCustomerByName(name).getId()).stream().map(
//                saleOrder -> new SaleOrderDto(saleOrder.getSOId(),
//                        saleOrder.getCustomer().getName(),
//                        saleOrder.getProducts().stream().map(g -> new ProductDto(
//                                g.getItem().getName(), g.getPriceForItem(), g.getQuantity(), g.getBoxCost(), g.getDiscount(), g.getNotes()
//                        )).toList()
//                        , saleOrder.getDate(),
//                        saleOrder.getTotalPrice(),
//                        saleOrder.getRemainAmount(), saleOrder.getNotes())).toList();
//
//    }

    public CustomerDataResponse getByCustomerName(String name) {

        List<SaleOrderDto> sos = getByCustomerId(customerService.getCustomerByName(name).getId()).stream().map(
                saleOrder -> new SaleOrderDto(saleOrder.getSOId(),
                        saleOrder.getCustomer().getName(),
                        saleOrder.getProducts().stream().map(g -> new ProductDto(
                                g.getItem().getName(), g.getPriceForItem(), g.getQuantity(), g.getBoxCost(), g.getDiscount(), g.getNotes()
                        )).toList()
                        , saleOrder.getDate(),
                        saleOrder.getTotalPrice(),
                        saleOrder.getRemainAmount(), saleOrder.getNotes())).toList();

        return new CustomerDataResponse(sos, sos.stream().mapToDouble(SaleOrderDto::getTotalPrice).sum(), sos.stream().mapToDouble(SaleOrderDto::getRemainAmount).sum());
    }
//    public List<SaleOrderDto> getByCustomerTotalAndRemain(String name) {
//        return getByCustomerId(customerService.getCustomerByName(name).getId()).stream().map(
//                saleOrder -> new SaleOrderDto(saleOrder.getSOId(),
//                        saleOrder.getCustomer().getName(),
//                        saleOrder.getProducts().stream().map(g -> new ProductDto(
//                                g.getItem().getName(), g.getPriceForItem(), g.getQuantity(), g.getBoxCost(), g.getDiscount(), g.getNotes()
//                        )).toList()
//                        , saleOrder.getDate(),
//                        saleOrder.getTotalPrice(),
//                        saleOrder.getRemainAmount(), saleOrder.getNotes())).toList();
//
//    }
}
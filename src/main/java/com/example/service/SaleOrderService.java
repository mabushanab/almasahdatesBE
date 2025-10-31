package com.example.service;

import com.example.dto.ProductDto;
import com.example.dto.SaleOrderDto;
import com.example.model.Products;
import com.example.model.SaleOrder;
import com.example.repository.SaleOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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

    public List<SaleOrderDto> getAllSaleOrders() {
        return saleOrderRepository.findAll().stream().
                map(saleOrder ->
                        new SaleOrderDto(saleOrder.getSOId(), saleOrder.getCustomer().getName(), saleOrder.getProducts().stream().map(g -> new ProductDto(
                                g.getItem().getName(), g.getPriceForItem(), g.getQuantity(), g.getBoxCost(), g.getNotes()
                        )).toList(), saleOrder.getDate(), saleOrder.getTotalPrice(),
                                saleOrder.getRemainAmount(), saleOrder.getNotes())).toList();
    }

    public String createSaleOrder(SaleOrderDto saleOrderDto) {
        long nextVal = getNextSequenceValue("sale_order");
        String soId = "SO-" + LocalDate.now().toString().replace("-", "")
                + "-" + String.format("%04d", nextVal);
        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setSOId(soId);
        saleOrder.setCustomer(customerService.getCustomerByName(saleOrderDto.getCustomerName()));
        saleOrder.setProducts(saleOrderDto.getProducts().stream().map(
                        g -> new Products(itemService.getEntityByName(g.getItemName()), g.getPriceForItem(), g.getQuantity(), g.getBoxCost(), g.getNotes()))
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
        saleOrderRepository.deleteById(id);
        return "The SaleOrder deleted successfully";
    }

    public List<SaleOrder> getByCustomerId(Long id) {
        return saleOrderRepository.getByCustomerId(id);
    }

    public byte[] generateInvoice(String customerName) {
        return pdfService.generateInvoiceSOs(customerName, saleOrderRepository
                .getByCustomerId(customerService.getCustomerByName(customerName)
                        .getId()).get(0).getProducts());
    }

    public byte[] generateInvoice2(String sOId) {
        return pdfService.generateInvoiceSOs(saleOrderRepository.getBysOId(sOId).getCustomer().getName(),
                saleOrderRepository.getBysOId(sOId).getProducts());
    }
}

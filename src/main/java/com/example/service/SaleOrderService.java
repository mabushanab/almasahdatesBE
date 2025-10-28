package com.example.service;

import com.example.dto.ProductDto;
import com.example.dto.SaleOrderDto;
import com.example.model.Customer;
import com.example.model.Products;
import com.example.model.SaleOrder;
import com.example.repository.SaleOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleOrderService {

    private final SaleOrderRepository saleOrderRepository;
    private final CustomerService customerService;
    private final ItemService itemService;

//    public SaleOrderDto getByName(String name) {
//        SaleOrder saleOrder = saleOrderRepository.findByName(name);
//
//           List<Products> products =
//        return new SaleOrderDto(saleOrder.getCustomer(), saleOrder.getProducts(),saleOrder.getDate(), saleOrder.getTotalPrice(),
//                 saleOrder.getRemainAmount(), saleOrder.getNotes());
//    }

    public List<SaleOrderDto> getAllSaleOrders() {
        return saleOrderRepository.findAll().stream().
                map(saleOrder ->
                        new SaleOrderDto(saleOrder.getCustomer().getName(), saleOrder.getProducts().stream().map(g -> new ProductDto(
                                g.getItem().getName(), g.getPriceForItem(), g.getQuantity(), g.getBoxCost(),g.getNotes()
                        )).toList(), saleOrder.getDate(), saleOrder.getTotalPrice(),
                                saleOrder.getRemainAmount(), saleOrder.getNotes())).toList();
    }

    public String createSaleOrder(SaleOrderDto saleOrderDto) {

        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setCustomer(customerService.getCustomerByName(saleOrderDto.getCustomerName()));
        saleOrder.setProducts(saleOrderDto.getProducts().stream().map(
                        g -> new Products(itemService.getEntityByName(g.getItemName()), g.getPriceForItem(), g.getQuantity(),g.getBoxCost(), g.getNotes()))
                .toList());
        saleOrder.setDate(LocalDate.now());
        saleOrder.setRemainAmount(saleOrderDto.getRemainAmount());
        saleOrder.setTotalPrice(saleOrderDto.getTotalPrice());
        saleOrder.setNotes(saleOrderDto.getNotes());
        saleOrderRepository.save(saleOrder);
        return "The Sale Order for customer : " + saleOrder.getCustomer().getName() + " saved successfully";
    }

    @Transactional
    public String deleteSaleOrder(Long id) {
        saleOrderRepository.deleteById(id);
        return "The SaleOrder deleted successfully";
    }

    public List<SaleOrder> getByCustomerId(Long id) {
        return saleOrderRepository.getByCustomerId(id);}

//    public String createSaleOrderList(List<SaleOrder> saleOrders) {
//        List<SaleOrder> p = saleOrders.stream().filter(saleOrder -> !saleOrderRepository.existsByName(saleOrder.getName())).toList();
//        if (p.isEmpty())
//            return "All saleOrders already exist";
//        else {
//            String S = p.stream().map(m -> m.getName() + ' ').toString();
//            saleOrderRepository.saveAll(p);
//            return "The SaleOrders: " + S + " saved successfully";
//        }
//
//    }
}
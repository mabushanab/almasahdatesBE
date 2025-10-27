package com.example.controller;

import com.example.dto.CustomerDto;
import com.example.model.Customer;
import com.example.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/list")
    public List<CustomerDto> getAllProducts2() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/create")
    public String createProduct(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @PostMapping("/createList")
    public String createProduct(@RequestBody List<Customer> customers) {
        return customerService.createCustomerList(customers);
    }

    @GetMapping("/{name}")
    public CustomerDto getUser(@PathVariable String name) {
        return customerService.getByName(name);
    }

    @DeleteMapping("/{name}")
    public String deleteItem(@PathVariable String name) {
        return customerService.deleteCustomer(name);
    }

}

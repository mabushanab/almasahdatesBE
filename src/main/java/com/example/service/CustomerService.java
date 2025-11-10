package com.example.service;

import com.example.dto.CustomerDto;
import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TenantServiceHelper tenantHelper;

    public CustomerDto getByName(String name) {
        tenantHelper.enableTenantFilter();
        Customer customer = customerRepository.findByName(name);

        return new CustomerDto(customer.getName(), customer.getType(), customer.getMobileNumber(), customer.getAddress(), customer.getRate(), customer.getNotes());
    }

    Customer getCustomerByName(String name) {
        tenantHelper.enableTenantFilter();return customerRepository.findByName(name);
    }

    public List<CustomerDto> getAllCustomers() {
        tenantHelper.enableTenantFilter();
        return customerRepository.findAll().stream().
                map(customer -> new CustomerDto(customer.getName(), customer.getType(), customer.getMobileNumber(), customer.getAddress(), customer.getRate(), customer.getNotes())).toList();
    }

    public String createCustomer(Customer customer) {
        tenantHelper.enableTenantFilter();
        if (customerRepository.existsByName(customer.getName()))
            throw new IllegalArgumentException("Customer already exist");
        else customerRepository.save(customer);
        return "The Customer: " + customer.getName() + " saved successfully";
    }

    @Transactional
    public String deleteCustomer(String name) {
        tenantHelper.enableTenantFilter();
        customerRepository.deleteByName(name);
        return "The Customer: " + name + " deleted successfully";
    }

    public String createCustomerList(List<Customer> customers) {
        tenantHelper.enableTenantFilter();
        List<Customer> p = customers.stream().filter(customer -> !customerRepository.existsByName(customer.getName())).toList();
        if (p.isEmpty())
            return "All customers already exist";
        else {
            String S = p.stream().map(m -> m.getName() + ' ').toString();
            customerRepository.saveAll(p);
            return "The Customers: " + S + " saved successfully";
        }

    }

    public List<Customer> getAllSaleOrdersEntities() {
        tenantHelper.enableTenantFilter();
        return customerRepository.findAll();
    }
}
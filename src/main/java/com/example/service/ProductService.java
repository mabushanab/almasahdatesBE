package com.example.service;

import com.example.dto.ProductDto;
import com.example.dto.UserDto;
import com.example.exception.UserNotFoundException;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.ClientInfoStatus;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

//    public List<Product> getAllUsers() {
//        return productRepository.findAll()
//                .stream()
//                .map(p -> new UserDto(p.getUsername(), p.getRole()))
//                .collect(Collectors.toList());
//    }
//
//    public UserDto getUserById(Long id) {
//        User user = productRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        return new UserDto(user.getUsername(), user.getRole());
//    }
//
//    public UserDto createUser(UserDto dto) {
//        User user = new User();
//        user.setUsername(dto.getUsername());
//        user.setRole(dto.getRole());
//        productRepository.save(user);
//        return new UserDto(user.getUsername(), user.getRole());
//    }

//    public ProductDto getProductByName(String name) {
//    }
//
//    public ProductDto getProductById(Long id) {
//    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().
                map(product -> new ProductDto(product.getName())).
                collect(Collectors.toList());
    }

    public List<Product> getAllProducts2() {
        return productRepository.findAll();
    }

    public ProductDto createProduct(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescr(dto.getDesc());
        product.setType(dto.getType());
        product.setSubtype(dto.getSubtype());
        productRepository.save(product);
        return new ProductDto(product.getName());
    }

    public String createProduct2(Product product) {
        if (productRepository.existsByName(product.getName()))
            throw new IllegalArgumentException("Product already exist");
        else productRepository.save(product);
        return "The Product: " + product.getName() + " saved successfully";
    }

    public String createProduct3(List<Product> products) {
        List<Product> p = products.stream().filter(product -> !productRepository.existsByName(product.getName())).toList();
        if (p.isEmpty())
            return "All products already exist";
        else {
            productRepository.saveAll(p);
            return "The Products saved successfully";
        }

    }
}
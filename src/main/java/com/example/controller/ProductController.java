package com.example.controller;

import com.example.dto.ProductDto;
import com.example.model.Product;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/list")
    public List<Product> getAllProducts2() {
        return productService.getAllProducts2();
    }

//    @GetMapping("/{id}")
//    public ProductDto getUser(@PathVariable Long id) {
//        return productService.getProductById(id);
//    }
//
//    @GetMapping("/{name}")
//    public ProductDto getUser(@PathVariable String name) {
//        return productService.getProductByName(name);
//    }

    @PostMapping
    public ProductDto createUser(@RequestBody ProductDto dto) {
        return productService.createProduct(dto);
    }

    @PostMapping("/create")
    public String createProduct(@RequestBody Product product) {
        return productService.createProduct2(product);
    }

    @PostMapping("/createList")
    public String createProduct(@RequestBody List<Product> products) {
        return productService.createProduct3(products);
    }
}

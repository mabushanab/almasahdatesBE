//package com.example.controller;
//
//import com.example.dto.ProductDto;
//import com.example.model.Item;
//import com.example.model.Merchant;
//import com.example.service.ItemService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/merchant")
//@RequiredArgsConstructor
//public class MerchantController {
//
//    private final ItemService itemService;
//
//    @GetMapping
//    public List<Merchant> getAllProducts() {
//        return itemService.getAllProducts();
//    }
//
//    @GetMapping("/list")
//    public List<ItemDto> getAllProducts2() {
//        return itemService.getAllItems();
//    }
//
////    @GetMapping("/{id}")
////    public ProductDto getUser(@PathVariable Long id) {
////        return productService.getProductById(id);
////    }
////
////    @GetMapping("/{name}")
////    public ProductDto getUser(@PathVariable String name) {
////        return productService.getProductByName(name);
////    }
//
//    @PostMapping
//    public ProductDto createUser(@RequestBody ProductDto dto) {
//        return itemService.createProduct(dto);
//    }
//
//    @PostMapping("/create")
//    public String createProduct(@RequestBody Item item) {
//        return itemService.createProduct2(item);
//    }
//
//    @PostMapping("/createList")
//    public String createProduct(@RequestBody List<Item> items) {
//        return itemService.createProductList(items);
//    }
//}

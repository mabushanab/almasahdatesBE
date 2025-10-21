package com.example.controller;

import com.example.dto.ItemDto;
import com.example.model.Item;
import com.example.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/list")
    public List<ItemDto> getAllProducts2() {
        return itemService.getAllItems();
    }

    @GetMapping("/{name}")
    public ItemDto getUser(@PathVariable String name) {
        return itemService.getByName(name);
    }

    @PostMapping("/create")
    public String createItem(@RequestBody Item item) {
        return itemService.createItem(item);
    }

    @DeleteMapping("/{name}")
    public String deleteItem(@PathVariable String name) {
        return itemService.deleteItem(name);
    }

    @PostMapping("/createList")
    public String createItem(@RequestBody List<Item> items) {
        return itemService.createItemList(items);
    }

//    @GetMapping("/{id}")
//    public ProductDto getUser(@PathVariable Long id) {
//        return productService.getProductById(id);
//    }
//

//    @GetMapping
//    public List<ItemDto> getAllProducts() {
//        return itemService.getAllItems();
//    }

//    @PostMapping
//    public ItemDto createUser(@RequestBody ItemDto dto) {
//        return itemService.createItem(dto);
//    }
}

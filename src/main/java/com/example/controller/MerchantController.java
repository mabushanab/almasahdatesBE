package com.example.controller;

import com.example.dto.ItemDto;
import com.example.dto.MerchantDto;
import com.example.model.Item;
import com.example.model.Merchant;
import com.example.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/list")
    public List<MerchantDto> getAllProducts2() {
        return merchantService.getAllMerchants();
    }

    @PostMapping("/create")
    public String createProduct(@RequestBody Merchant merchant) {
        return merchantService.createMerchant(merchant);
    }

    @PostMapping("/createList")
    public String createProduct(@RequestBody List<Merchant> merchants) {
        return merchantService.createMerchantList(merchants);
    }

    @GetMapping("/{name}")
    public MerchantDto getUser(@PathVariable String name) {
        return merchantService.getByName(name);
    }

    @DeleteMapping("/{name}")
    public String deleteItem(@PathVariable String name) {
        return merchantService.deleteMerchant(name);
    }

}

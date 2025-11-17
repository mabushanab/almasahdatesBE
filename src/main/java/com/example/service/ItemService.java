package com.example.service;

import com.example.dto.ItemDto;
import com.example.model.Item;
import com.example.model.Store;
import com.example.repository.ItemRepository;
import com.example.repository.StoreRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final StoreService storeService;
    private final TenantServiceHelper tenantHelper;


    public ItemDto getByName(String name) {
        tenantHelper.enableTenantFilter();
        Item item = itemRepository.findByName(name);

        return new ItemDto(item.getName(), item.getSalePrice(), item.getType(), item.getSubType(), item.getDescr());
    }

    public Item getEntityByName(String name) {
        tenantHelper.enableTenantFilter();
        return itemRepository.findByName(name);
    }

    public List<Item> getAllItemsEntities() {
        tenantHelper.enableTenantFilter();
        return itemRepository.findAll();
    }

    public List<ItemDto> getAllItems() {
        tenantHelper.enableTenantFilter();
        return itemRepository.findAll().stream().
                map(item -> new ItemDto(item.getName(), item.getSalePrice(), item.getType(), item.getSubType(), item.getDescr())).toList();
    }

    public String createItem(Item item) {
        tenantHelper.enableTenantFilter();
//        storeService.createUser()
        if (itemRepository.existsByName(item.getName())) {
            throw new IllegalArgumentException("Item already exist");
        } else {
            itemRepository.save(item);
        }
        return "The Item: " + item.getName() + " saved successfully";
    }

    @Transactional
    public String deleteItem(String name) {
        tenantHelper.enableTenantFilter();
        itemRepository.deleteByName(name);
        return "The Item: " + name + " deleted successfully";
    }

    public String createItemList(List<Item> items) {
        tenantHelper.enableTenantFilter();
        List<Item> p = items.stream().filter(item -> !itemRepository.existsByName(item.getName())).toList();
        if (p.isEmpty())
            return "All items already exist";
        else {
            itemRepository.saveAll(p);
            return "The Items: " + p + " saved successfully";
        }

    }

    public String setSalePrice(String name, double price) {
        tenantHelper.enableTenantFilter();
        Item item = itemRepository.findByName(name);
        item.setSalePrice(price);
        itemRepository.save(item);
        return "The item " + name + " new price is " + price;
    }

    public double getSalePrice(String name) {
        tenantHelper.enableTenantFilter();
        return itemRepository.findByName(name).getSalePrice();
    }
}
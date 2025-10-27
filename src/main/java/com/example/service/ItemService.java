package com.example.service;

import com.example.dto.ItemDto;
import com.example.model.Item;
import com.example.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

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

    public ItemDto getByName(String name) {
        Item item = itemRepository.findByName(name);

        return new ItemDto(item.getName(), item.getType(), item.getSubtype(), item.getDescr());
    }

    public Item getEntityByName(String name) {

        return itemRepository.findByName(name);
    }

    public List<Item> getAllItemsEntities() {
        return itemRepository.findAll();
    }

    public List<ItemDto> getAllItems() {
        return itemRepository.findAll().stream().
                map(item -> new ItemDto(item.getName(), item.getType(), item.getSubtype(), item.getDescr())).toList();
    }

//    public List<Item> getAllProducts2() {
//        return itemRepository.findAll();
//    }

//    public ItemDto createItem(ItemDto dto) {
//        Item item = new Item(dto.getName(),dto.getDesc(),dto.getType(),dto.getSubtype());
//        itemRepository.save(new Item(dto.getName(),dto.getDesc(),dto.getType(),dto.getSubtype()));
//        return new ItemDto(item);
//    }

    public String createItem(Item item) {
        if (itemRepository.existsByName(item.getName()))
            throw new IllegalArgumentException("Item already exist");
        else itemRepository.save(item);
        return "The Item: " + item.getName() + " saved successfully";
    }

    @Transactional
    public String deleteItem(String name) {
        itemRepository.deleteByName(name);
        return "The Item: " + name + " deleted successfully";
    }

    public String createItemList(List<Item> items) {
        List<Item> p = items.stream().filter(item -> !itemRepository.existsByName(item.getName())).toList();
        if (p.isEmpty())
            return "All items already exist";
        else {
            itemRepository.saveAll(p);
            return "The Items: " + p + " saved successfully";
        }

    }
}
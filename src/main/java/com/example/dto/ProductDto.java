package com.example.dto;

import com.example.model.Item;
import com.example.model.UserRole;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String itemName;
    private double priceForItem;
    private int quantity;
    private double boxCost;
    private int discount;
    private String notes;
}

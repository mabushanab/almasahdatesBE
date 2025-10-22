package com.example.dto;

import com.example.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDto {
    private String itemName;
    private float priceForGrams;
    private float weightInGrams;
    private String notes;


}

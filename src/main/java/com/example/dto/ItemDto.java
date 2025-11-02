package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ItemDto {
    public ItemDto(String name){
        this.name = name;
    }
    private String name;
    private double salePrice;
    private String type;
    private String subtype;
    private String desc;
}

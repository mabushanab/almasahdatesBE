package com.example.dto;

import com.example.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductDto {
    public ProductDto(String name){
        this.name = name;
    }
    private String name;
    private String type;
    private String subtype;
    private String desc;
}

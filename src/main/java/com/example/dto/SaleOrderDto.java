package com.example.dto;

import com.example.model.Item;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrderDto {
    private String customerName;
    private List<ProductDto> products;
    private LocalDate date;
    private float totalPrice;
    private float remainAmount;
    private String notes;

}

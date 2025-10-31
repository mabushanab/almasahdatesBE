package com.example.dto;

import com.example.model.Goods;
import com.example.model.Merchant;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDto {

    private String pOId;
    private String merchantName;
    private List<GoodsDto> goods;
    private LocalDate date;
    private float totalPrice;
    private float remainAmount;
    private String notes;
}

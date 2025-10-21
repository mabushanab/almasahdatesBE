package com.example.dto;

import com.example.model.Goods;
import com.example.model.Merchant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDto {
    private Merchant marchent;
    private List<Goods> goods;
    private Date date;
    private float totalPrice;
    private float remainAmount;
    private String notes;
}

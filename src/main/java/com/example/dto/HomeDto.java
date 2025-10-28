package com.example.dto;

import com.example.model.PurchaseOrder;
import com.example.model.SaleOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeDto {

    double sumPO;
    double sumPORemain;
    double sumSO;
    double sumSORemain;
    Map<String, Double> avgProductPrice;
    Map<String, Double> avgGoodPrice;
    Map<String, Double> avgProductPriceWithBox;
    Map<String, Double> avgProfitPerItem;
    Map<String, Double> totalProfitPerItem;
    Map<String, Double> MerchantRemainAmount;
    Map<String, List<PurchaseOrder>> merchantPOs;
    Map<String, List<SaleOrder>> customerSOs;


}

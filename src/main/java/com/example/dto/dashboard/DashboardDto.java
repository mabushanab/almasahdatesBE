package com.example.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {

//    double pOs;
    double sumPO;
    double sumPORemain;
//    double sOs;
    double sumSO;
    double sumSORemain;
    Map<String, Double> avgProductPrice;
    Map<String, Double> avgGoodPrice;
    Map<String, Double> avgProductPriceWithBox;
    Map<String, Double> avgProfitPerItem;
    Map<String, Double> totalProfitPerItem;
    Map<String, Double> MerchantTotalPrice;
    Map<String, Double> MerchantRemainAmount;


}

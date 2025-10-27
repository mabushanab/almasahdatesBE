package com.example.dto;

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

}

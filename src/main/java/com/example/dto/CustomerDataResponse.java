package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor 
public class CustomerDataResponse {

    private List<SaleOrderDto> saleOrderDtos;
    private double remain;
    private double total;
//    private double

}

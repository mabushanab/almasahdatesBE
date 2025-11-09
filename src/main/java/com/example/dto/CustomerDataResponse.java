package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor
public class CustomerDataResponse {

    private List<SaleOrderDto> saleOrders;
    private double total;
    private double remain;
//    private double

}

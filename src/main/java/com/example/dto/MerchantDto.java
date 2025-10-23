package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDto {

    private String name;
    private String type;
    private String mobileNumber;
    private String address;
    private int rate;

    private String notes;

}

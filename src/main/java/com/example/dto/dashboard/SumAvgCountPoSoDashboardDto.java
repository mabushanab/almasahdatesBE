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
public class SumAvgCountPoSoDashboardDto {

    int pOs;
    double sumPO;
    double sumPORemain;
    int sOs;
    double sumSO;
    double sumSORemain;
}

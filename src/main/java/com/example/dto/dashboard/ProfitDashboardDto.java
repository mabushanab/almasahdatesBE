package com.example.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfitDashboardDto {

    double profitPercent;
    double profitPercentLast1Month;
    double profitPercentLast3Month;
}

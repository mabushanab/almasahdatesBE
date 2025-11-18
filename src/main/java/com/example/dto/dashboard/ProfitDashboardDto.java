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

    int profitPercent;
    int profitPercentLast1Month;
    int profitPercentLast3Month;
}

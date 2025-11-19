package com.example.controller;

import com.example.dto.dashboard.DashboardDto;
import com.example.dto.dashboard.ProfitDashboardDto;
import com.example.dto.dashboard.SumAvgCountPoSoDashboardDto;
import com.example.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/poSoDashboard")
    public SumAvgCountPoSoDashboardDto getPoSoDashboard() {
        return dashboardService.getPoSoInfo();
    }
    @GetMapping("/profitInfo")
    public ProfitDashboardDto getProfitInfo() {
        return dashboardService.getProfitInfo();
    }
}
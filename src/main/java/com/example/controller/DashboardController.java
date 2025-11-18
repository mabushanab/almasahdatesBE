package com.example.controller;

import com.example.dto.dashboard.DashboardDto;
import com.example.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/CDD")
    public DashboardDto getCreditDebitDetails() {
        return dashboardService.getCreditDebitDetails();
    }
}

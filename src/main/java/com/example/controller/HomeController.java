package com.example.controller;

import com.example.dto.HomeDto;
//import com.example.model.Home;
import com.example.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;


    @GetMapping("/CDD")
    public HomeDto getCreditDebitDetails() {
        return homeService.getCreditDebitDetails();
    }

}

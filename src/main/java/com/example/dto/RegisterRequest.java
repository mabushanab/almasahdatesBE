package com.example.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
}

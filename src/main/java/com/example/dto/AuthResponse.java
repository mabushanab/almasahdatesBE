package com.example.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor 
public class AuthResponse {

    public AuthResponse(String token) {
        this.token = token;
    }
    private String token;
}

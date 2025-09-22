package com.example.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getPassword() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

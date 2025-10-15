package com.example.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;

//    public Object getPassword() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
}

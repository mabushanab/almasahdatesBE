package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // disable CSRF for H2 console
            .headers(headers -> headers.frameOptions().disable()) // allow frames
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() // allow H2
                .anyRequest().permitAll() // allow all other requests
            );
        return http.build();
    }
}

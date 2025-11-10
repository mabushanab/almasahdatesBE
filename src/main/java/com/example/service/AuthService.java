package com.example.service;

import com.example.dto.*;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final TenantServiceHelper tenantHelper;

    public String register(RegisterRequest request) {
//        tenantHelper.enableTenantFilter();
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTenantId(request.getTenantId());
        user.setRole(UserRole.USER);
        userRepository.save(user);
        return jwtUtil.generateToken(user);
    }

    public String login(LoginRequest request) {
//        tenantHelper.enableTenantFilter();
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        return jwtUtil.generateToken(userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }
}

package com.example.service;

import com.example.dto.UserDto;
import com.example.exception.UserNotFoundException;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserDto(u.getUsername(), u.getRole()))
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserDto(user.getUsername(), user.getRole());
    }

    public UserDto createUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        userRepository.save(user);
        return new UserDto(user.getUsername(), user.getRole());
    }
}

package com.finpulse.fin.service.impl;

import com.finpulse.fin.dto.*;
import com.finpulse.fin.entity.User;
import com.finpulse.fin.repository.UserRepository;
import com.finpulse.fin.security.JwtService;
import com.finpulse.fin.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        log.info("Register request for email: {}",
                request.getEmail());

        // CHECK EXISTING EMAIL
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new RuntimeException(
                            "Email already exists");
                });

        // CREATE USER
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .role("USER")
                .build();

        // SAVE USER
        userRepository.save(user);

        // GENERATE JWT
        String token = jwtService.generateToken(
                user.getEmail());

        log.info("User registered successfully");

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        log.info("Login request for email: {}",
                request.getEmail());

        // FIND USER
        User user = userRepository.findByEmail(
                        request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        // CHECK PASSWORD
        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword());

        if (!matches) {

            log.error("Invalid password");

            throw new RuntimeException(
                    "Invalid credentials");
        }

        // GENERATE JWT
        String token = jwtService.generateToken(
                user.getEmail());

        log.info("Login successful");

        return new AuthResponse(token);
    }
}
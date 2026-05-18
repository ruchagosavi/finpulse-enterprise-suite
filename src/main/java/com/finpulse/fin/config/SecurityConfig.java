package com.finpulse.fin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.Customizer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // DISABLE CSRF as it was giving 403 forbidden error on POSTMAN its dfferent from 401 
                // 401 is for unauthorized access and 403 is for forbidden access which means you are authenticated but not authorized to access the resource
                .csrf(csrf -> csrf.disable())

                // AUTHORIZATION RULES
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC AUTH APIs
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // OTHER APIS REQUIRE AUTH
                        .anyRequest()
                        .authenticated()
                )

                // DISABLE DEFAULT LOGIN PAGE
                .formLogin(form -> form.disable())

                // DISABLE HTTP BASIC
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
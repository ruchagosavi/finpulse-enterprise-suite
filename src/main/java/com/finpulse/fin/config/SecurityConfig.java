package com.finpulse.fin.config;

import com.finpulse.fin.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // Disable CSRF as we are not using cookies for session management and we are using JWT token for authentication and authorization
                .csrf(csrf -> csrf.disable())

                // Stateless session management as we dont want to store any session information on the server side as we are using JWT token for authentication and authorization
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // Public auth APIs
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // All other APIs protected
                        .anyRequest()
                        .authenticated()
                )

                // Disable form login
                .formLogin(form -> form.disable())

                // Disable HTTP basic auth as we dont want username and password to flow every time in the request header instead we will use JWT token for authentication and authorization
                .httpBasic(Customizer.withDefaults())

                // Add JWT filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
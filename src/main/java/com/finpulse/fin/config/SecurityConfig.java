package com.finpulse.fin.config;

import com.finpulse.fin.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
        // Disable CSRF as we are not using cookies for session management and we are using JWT token for authentication and authorization
                .csrf(csrf -> csrf.disable())

                 // Stateless session management as we dont want to store any session information on the server side as we are using JWT token for authentication and authorization
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                // Public auth APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()
                // All other APIs protected
                        .anyRequest()
                        .authenticated()
                )

                // Disable form login so that we dont have to provide username and password in the request body instead we will use JWT token for authentication and authorization
                .formLogin(form -> form.disable())

                // Disable HTTP basic auth as we dont want username and password to flow every time in the request header instead we will use JWT token for authentication and authorization
                .httpBasic(httpBasic -> httpBasic.disable())

                // Add JWT filter to the security filter chain so that it can intercept the request and validate the JWT token before it reaches the controller
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
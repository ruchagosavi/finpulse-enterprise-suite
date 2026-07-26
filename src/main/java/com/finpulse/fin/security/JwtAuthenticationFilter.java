package com.finpulse.fin.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        final String jwt;

        final String userEmail;

        // No token present some api might be public, so just continue filter chain so that the request can be processed by the next filter or the target resource
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }

        // Extract token it removes "Bearer " from the header value
        jwt = authHeader.substring(7);

        // Extract email from token to identify user
        userEmail = jwtService.extractUsername(jwt);

        // If user not already authenticated and token is valid then authenticate user this is to avoid duplication of authentication in the filter chain
        if (userEmail != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Validate token and if valid then set authentication in the security context
            if (jwtService.isTokenValid(jwt, userEmail)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userEmail,
                                null,
                                Collections.emptyList()
                        );
            // Set details of the authentication token using the request
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
            // Set the authentication in the security context this is used by Spring Security to authorize the user for the current request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    // Continue with the filter chain to allow the request to proceed to the next filter or the target resource so that the request can be processed by the next filter or the target resource
        filterChain.doFilter(request, response);
    }
}

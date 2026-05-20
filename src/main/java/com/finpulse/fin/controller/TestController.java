package com.finpulse.fin.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String test() {
        return "Protected API Working";
    }

    @GetMapping("/me")
    public String currentUser() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
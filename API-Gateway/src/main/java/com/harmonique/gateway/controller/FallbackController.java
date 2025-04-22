package com.harmonique.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Harmonique API Gateway!";
    }

    @GetMapping("/error")
    public String error() {
        return "Custom Error Page - Route not found";
    }
}

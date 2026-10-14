package com.ecocity.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @GetMapping("/health")
    public Map<String, String> status() {
        return Map.of(
            "status", "UP",
            "environment", activeProfile,
            "system", "EcoCity ESG Platform"
        );
    }
}

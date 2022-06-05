package com.c99.innovation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/checkHealth")
    public ResponseEntity<Object> checkHealth() {
        return ResponseEntity.ok("OK");
    }
}

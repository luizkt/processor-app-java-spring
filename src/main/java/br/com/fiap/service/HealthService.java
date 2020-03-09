package br.com.fiap.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthService {
    @GetMapping("/health")
    public String index() {
        return "UP";
    }
}
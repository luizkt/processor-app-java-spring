package br.com.fiap.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {
    public String healthCheck() {
        return "UP";
    }
}
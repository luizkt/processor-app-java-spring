package br.com.fiap.service.impl;

import br.com.fiap.service.HealthService;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImpl implements HealthService {

    @Override
    public String healthCheck() {
        return "UP";
    }
}
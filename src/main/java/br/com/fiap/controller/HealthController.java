package br.com.fiap.controller;

import br.com.fiap.service.HealthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Autowired
    HealthService healthService;

    @GetMapping("/health")
    @ApiOperation(value = "Health check")
    @ApiResponse(code = 200, message = "Health check")
    public String healthCheck() {
        return healthService.healthCheck();
    }
}
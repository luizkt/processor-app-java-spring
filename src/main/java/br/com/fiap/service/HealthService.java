package br.com.fiap.service;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthService {
    @GetMapping("/health")
    @ApiOperation(value = "Health check")
    @ApiResponse(code = 200, message = "Health check")
    public String index() {
        return "UP";
    }
}
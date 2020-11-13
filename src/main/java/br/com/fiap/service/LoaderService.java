package br.com.fiap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface LoaderService {
    ResponseEntity<String> loadFromCsv() throws JsonProcessingException;
}

package br.com.fiap.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Log4j2
public class ErrorResponse {
    public static ResponseEntity<String> build(Exception exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        String body = "{\"message\":\"An error has occurred\", \"exception\":" + exception.getMessage() + "}";

        log.error("Full stack trace:", exception);

        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }
}

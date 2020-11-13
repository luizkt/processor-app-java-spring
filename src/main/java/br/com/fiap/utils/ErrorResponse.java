package br.com.fiap.utils;

import br.com.fiap.entity.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@Log4j2
public class ErrorResponse {
    public static ResponseEntity<String> build(Exception exception) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        ResponseBody responseBody = new ResponseBody("An error has occurred", exception.getMessage());

        log.error(exception.getMessage());
        log.debug("Full stack trace:", exception);

        return new ResponseEntity<>(mapper.writeValueAsString(responseBody), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<String> build(HttpClientErrorException exception, HttpStatus httpStatus) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        ResponseBody responseBody = new ResponseBody(exception.getStatusText());

        log.error(exception.getMessage());
        log.debug("Full stack trace:", exception);

        return new ResponseEntity<>(mapper.writeValueAsString(responseBody), headers, httpStatus);
    }
}

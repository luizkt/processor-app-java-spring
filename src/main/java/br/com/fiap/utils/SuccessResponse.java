package br.com.fiap.utils;

import br.com.fiap.entity.ResponseBody;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Log4j2
public class SuccessResponse {
    public static ResponseEntity<String> build(ResponseBody responseBody,HttpStatus httpStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        log.info(responseBody.toJsonString());

        return new ResponseEntity<>(responseBody.toJsonString(), headers, httpStatus);
    }
}

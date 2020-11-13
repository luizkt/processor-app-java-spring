package br.com.fiap.utils;

import br.com.fiap.entity.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Log4j2
public class SuccessResponse {
    public static ResponseEntity<String> build(ResponseBody responseBody,HttpStatus httpStatus) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        ObjectMapper mapper = new ObjectMapper();

        log.info(responseBody.getMessage());
        log.debug(mapper.writeValueAsString(responseBody));

        return new ResponseEntity<>(mapper.writeValueAsString(responseBody), headers, httpStatus);
    }
}

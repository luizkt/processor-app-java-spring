package br.com.fiap.utils;

import br.com.fiap.entity.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test"})
public class ErrorResponseTest {

    @Test
    public void givenAUnknownErrorResponse_whenBuildingIt_shouldReturnErrorResponseEntity() throws IOException {
        ResponseEntity result = ErrorResponse.build(new Exception("Some Message"));

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody resultResponseBody = mapper.readValue((String) result.getBody(), ResponseBody.class);

        assertNotNull(result);
        assertEquals("An error has occurred", resultResponseBody.getMessage());
        assertEquals("Some Message", resultResponseBody.getException());
    }

    @Test
    public void givenAErrorResponse_whenBuildingIt_shouldReturnErrorResponseEntity() throws IOException {
        ResponseEntity result = ErrorResponse.build(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Some Message"), HttpStatus.BAD_REQUEST);

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody resultResponseBody = mapper.readValue((String) result.getBody(), ResponseBody.class);

        assertNotNull(result);
        assertEquals("Some Message", resultResponseBody.getMessage());
    }
}

package br.com.fiap.utils;

import br.com.fiap.entity.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test"})
public class SuccessResponseTest {

    @Test
    public void givenASuccessResponse_whenBuildingIt_shouldReturnSuccessResponseEntity() throws IOException {
        ResponseEntity result = SuccessResponse.build(new ResponseBody("Some Message", "{\"message\": \"Some json string\""), HttpStatus.OK);

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody resultResponseBody = mapper.readValue((String) result.getBody(), ResponseBody.class);

        assertNotNull(result);
        assertEquals("Some Message", resultResponseBody.getMessage());
    }
}

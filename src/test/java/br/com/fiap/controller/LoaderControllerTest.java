package br.com.fiap.controller;

import br.com.fiap.service.impl.LoaderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test"})
public class LoaderControllerTest {

    @Mock
    private LoaderServiceImpl loaderService;

    private MvcResult result;

    private MockMvc mockMvc;

    LoaderController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        controller = new LoaderController(loaderService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void givenStudentsAndCsvFile_whenRegisteringIt_shouldAddAllStudentsSuccessfully() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        String body = "{\"message\":\"Added all the students and transactions successfully\"}";

        when(loaderService.loadFromCsv()).thenReturn(new ResponseEntity<String>(body, headers, HttpStatus.CREATED));

        result = mockMvc.perform(MockMvcRequestBuilders.post("/loader/load_from_csv")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertNotNull(result);
        assertEquals(201, result.getResponse().getStatus());
    }
}

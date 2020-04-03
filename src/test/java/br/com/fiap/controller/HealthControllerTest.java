package br.com.fiap.controller;

import br.com.fiap.service.HealthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test"})
public class HealthControllerTest {

    @Mock
    HealthService healthService;

    private MvcResult result;

    private MockMvc mockMvc;

    HealthController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        controller = new HealthController(healthService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void givenHealthCheck_whenCheckingIfApplicationIsUp_shouldReturnUP() throws Exception {

        when(healthService.healthCheck()).thenReturn("UP");

        result = mockMvc.perform(MockMvcRequestBuilders.get("/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("UP", result.getResponse().getContentAsString());

    }
}

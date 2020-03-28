package br.com.fiap.service;

import br.com.fiap.ProcessorApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProcessorApplication.class)
@ActiveProfiles({"test"})
public class HealthServiceTest {

    @Autowired
    HealthService healthService;

    @Test
    public void shouldReturnUPSuccessfully() {
        String status = healthService.healthCheck();

        assertEquals("UP", status);
    }
}

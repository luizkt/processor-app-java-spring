package br.com.fiap.service;

import br.com.fiap.controller.StudentController;
import br.com.fiap.entity.Student;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test"})
public class LoaderServiceTest {

    @Mock
    StudentRepository studentRepository;

    @Mock
    TransactionRepository transactionRepository;

    LoaderService service;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        service = new LoaderService(studentRepository, transactionRepository);

        this.mockMvc = MockMvcBuilders.standaloneSetup(service).build();
    }

    @Test
    public void givenStudentCsvFile_whenReadingIt_shouldReturnListOfStudents() throws IOException {
        ResponseEntity<String> response = service.loadFromCsv();

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
    }
}

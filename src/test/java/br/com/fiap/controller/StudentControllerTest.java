package br.com.fiap.controller;

import br.com.fiap.entity.Student;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.service.StudentService;
import br.com.fiap.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.xml.ws.Response;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test"})
public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private StudentRepository studentRepository;

    private MvcResult result;

    private MockMvc mockMvc;

    StudentController controller;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

        controller = new StudentController(studentService, transactionService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void givenNewStudent_whenRegisteringHim_shouldAddStudentSuccessfully() throws Exception {

        Student student = new Student(333000, "Student name");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        String body = "{\"message\":\"Added the student successfully\"}";

        when(studentService.add(student)).thenReturn(new ResponseEntity<String>(body, headers, HttpStatus.CREATED));

        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(student)))
                .andExpect(status().is2xxSuccessful());

    }

}

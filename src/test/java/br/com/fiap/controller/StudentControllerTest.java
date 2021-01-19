package br.com.fiap.controller;

import br.com.fiap.entity.ApplicationResponseBody;
import br.com.fiap.entity.Student;
import br.com.fiap.service.impl.StudentServiceImpl;
import br.com.fiap.service.impl.TransactionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test"})
public class StudentControllerTest {

    @Mock
    private StudentServiceImpl studentService;

    @Mock
    private TransactionServiceImpl transactionService;

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

        ApplicationResponseBody applicationResponseBody = new ApplicationResponseBody("Added the student successfully", student);

        ObjectMapper mapper = new ObjectMapper();

        when(studentService.add(Mockito.any(Student.class)))
                .thenReturn(applicationResponseBody);

        result = mockMvc.perform(MockMvcRequestBuilders.post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(student)))
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertNotNull(result);
        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    public void givenRegisteredStudent_whenUpdatingHim_shouldUpdateStudentSuccessfully() throws Exception {
        Student student = new Student();
        student.setName("New student name");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        ApplicationResponseBody applicationResponseBody = new ApplicationResponseBody("Updated the student successfully", student);

        ObjectMapper mapper = new ObjectMapper();

        when(studentService.updateStudentByStudentRegistrationNumber(Mockito.any(Student.class), eq(333000)))
                .thenReturn(applicationResponseBody);

        result = mockMvc.perform(MockMvcRequestBuilders.patch("/students/333000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(student)))
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void givenRegisteredStudent_whenDeletingHim_shouldRemoveFromRegistration() throws Exception {
        Student student = new Student(333000, "Student Name");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        ApplicationResponseBody applicationResponseBody = new ApplicationResponseBody("Deleted the student successfully", student);

        ObjectMapper mapper = new ObjectMapper();

        when(studentService.deleteStudentByStudentRegistrationNumber(student.getStudentRegistrationNumber()))
                .thenReturn(applicationResponseBody);

        result = mockMvc.perform(MockMvcRequestBuilders.delete("/students/333000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void givenRegisteredStudent_whenSearchingForHisName_shouldReturnStudents() throws Exception {
        List<Student> students = new ArrayList<>();

        students.add(new Student(111000, "Name 0"));
        students.add(new Student(222000, "Name 1"));
        students.add(new Student(333000, "Name 2"));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        ApplicationResponseBody applicationResponseBody = new ApplicationResponseBody("Search for student's name successfully", students);

        ObjectMapper mapper = new ObjectMapper();

        when(studentService.findByName(Mockito.anyString())).thenReturn(applicationResponseBody);

        result = mockMvc.perform(MockMvcRequestBuilders.get("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "Name"))
                .andExpect(status().is2xxSuccessful()).andReturn();

        ApplicationResponseBody resultApplicationResponseBody = mapper.readValue(result.getResponse().getContentAsString(), ApplicationResponseBody.class);
        List<Student> resultStudents = Arrays.asList(mapper.convertValue(resultApplicationResponseBody.getData(), Student[].class));

        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(3, resultStudents.size());
    }

    @Test
    public void givenRegisteredStudent_whenSearchingWithRegistrationNumber_shouldReturnStudent() throws Exception {
        Student student = new Student(111000, "Name");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        ApplicationResponseBody applicationResponseBody = new ApplicationResponseBody("Search for student's registration number successfully", student);

        ObjectMapper mapper = new ObjectMapper();

        when(studentService.findByStudentRegistrationNumber(student.getStudentRegistrationNumber()))
                .thenReturn( applicationResponseBody);

        result = mockMvc.perform(MockMvcRequestBuilders.get("/students/111000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        ApplicationResponseBody resultApplicationResponseBody = mapper.readValue(result.getResponse().getContentAsString(), ApplicationResponseBody.class);
        Student resultStudent = mapper.convertValue(resultApplicationResponseBody.getData(), Student.class);

        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(resultStudent.getName(), student.getName());
        assertEquals(resultStudent.getStudentRegistrationNumber(), student.getStudentRegistrationNumber());
    }

}

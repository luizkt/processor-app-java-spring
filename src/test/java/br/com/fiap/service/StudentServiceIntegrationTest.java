package br.com.fiap.service;

import br.com.fiap.ProcessorApplication;
import br.com.fiap.config.ProcessorMySqlContainer;
import br.com.fiap.entity.ResponseBody;
import br.com.fiap.entity.Student;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.service.impl.StudentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProcessorApplication.class)
@ActiveProfiles({"integrationTest"})
public class StudentServiceIntegrationTest {

    @ClassRule
    public static MySQLContainer processorMySqlContainer = ProcessorMySqlContainer.getInstance();

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Before
    @Transactional("transactionManager")
    public void setUp() {
        studentRepository.save(mockStudent());
    }

    @Test
    public void givenNewStudent_whenRegisteringHim_shouldRegisterNewStudent() throws IOException {

        ResponseEntity<String> response = studentService.add(new Student(111000, "New Student Name"));

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody responseBody = mapper.readValue(response.getBody(), ResponseBody.class);
        Student student = mapper.convertValue(responseBody.getData(), Student.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("New Student Name", student.getName());
    }

    @Test
    public void givenRegisteredStudent_whenRegisteringHim_shouldThrowAnError() throws IOException {

        ResponseEntity<String> response = studentService.add(mockStudent());

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody responseBody = mapper.readValue(response.getBody(), ResponseBody.class);
        Student student = mapper.convertValue(responseBody.getData(), Student.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(responseBody.getMessage().equals("Student registration number already exist"));
    }

    @Test
    public void givenRegisteredStudent_whenUpdatingHim_shouldUpdateStudentSuccessfully() throws IOException {

        ResponseEntity<String> response = studentService.updateStudentByStudentRegistrationNumber(new Student(
                mockStudent().getStudentRegistrationNumber(),
                "New Name"
        ), mockStudent().getStudentRegistrationNumber());

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody responseBody = mapper.readValue(response.getBody(), ResponseBody.class);
        Student student = mapper.convertValue(responseBody.getData(), Student.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("New Name", student.getName());
    }

    @Test
    public void givenNotRegisteredStudent_whenUpdatingHim_shouldThrowAnError() throws IOException {

        ResponseEntity<String> response = studentService.updateStudentByStudentRegistrationNumber(new Student(
                123123,
                "New Name"
        ), 123123);

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody responseBody = mapper.readValue(response.getBody(), ResponseBody.class);
        Student student = mapper.convertValue(responseBody.getData(), Student.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(responseBody.getMessage().equals("Student registration number doesn't exist"));
    }

    @Test
    public void givenRegisteredStudent_whenSearchingByHisName_shouldFindStudentSuccessfully() throws IOException {

        ResponseEntity<String> response = studentService.findByName(mockStudent().getName());

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody responseBody = mapper.readValue(response.getBody(), ResponseBody.class);
        List<Student> students = Arrays.asList(mapper.convertValue(responseBody.getData(), Student[].class));

        assertEquals(mockStudent().getName(), students.get(0).getName());
    }

    @Test
    public void givenRegisteredStudent_whenSearchingByHisRegisterNumber_shouldFindStudentSuccessfully() throws IOException {

        ResponseEntity<String> response = studentService.findByStudentRegistrationNumber(mockStudent().getStudentRegistrationNumber());

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody responseBody = mapper.readValue(response.getBody(), ResponseBody.class);
        Student student = mapper.convertValue(responseBody.getData(), Student.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(mockStudent().getStudentRegistrationNumber(), student.getStudentRegistrationNumber());
    }

    @Test
    public void givenRegisteredStudent_whenDeletingHim_shouldDeleteStudentSuccessfully() throws IOException {

        ResponseEntity<String> response = studentService.deleteStudentByStudentRegistrationNumber(mockStudent().getStudentRegistrationNumber());

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody responseBody = mapper.readValue(response.getBody(), ResponseBody.class);
        Student student = mapper.convertValue(responseBody.getData(), Student.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(mockStudent().getStudentRegistrationNumber(), student.getStudentRegistrationNumber());
    }

    @Test
    public void givenNotRegisteredStudent_whenDeletingHim_shouldThrowAnError() throws IOException {

        ResponseEntity<String> response = studentService.deleteStudentByStudentRegistrationNumber(1);

        ObjectMapper mapper = new ObjectMapper();
        ResponseBody responseBody = mapper.readValue(response.getBody(), ResponseBody.class);
        Student student = mapper.convertValue(responseBody.getData(), Student.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(responseBody.getMessage().equals("Student registration number doesn't exist"));
    }

    private Student mockStudent() {
        return new Student(333000, "Student Name");
    }
}

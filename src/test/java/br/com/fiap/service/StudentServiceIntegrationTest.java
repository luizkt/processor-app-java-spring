package br.com.fiap.service;

import br.com.fiap.ProcessorApplication;
import br.com.fiap.config.ProcessorMySqlContainer;
import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

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
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Before
    @Transactional("transactionManager")
    public void setUp() {
        studentRepository.save(mockStudent());
    }

    @Test
    public void givenNewStudent_whenRegisteringHim_shouldRegisterNewStudent() {

        ResponseEntity<String> response = studentService.add(new Student(111000, "New Student Name"));

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void givenRegisteredStudent_whenRegisteringHim_shouldThrowAnError() {

        ResponseEntity<String> response = studentService.add(mockStudent());

        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    public void givenRegisteredStudent_whenUpdatingGim_shouldUpdateStudentSuccessfully() {

        ResponseEntity<String> response = studentService.updateStudentByStudentRegistrationNumber(new Student(
                mockStudent().getStudentRegistrationNumber(),
                "New Name"
        ), mockStudent().getStudentRegistrationNumber());

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void givenNotRegisteredStudent_whenUpdatingHim_shouldThrowAnError() {

        ResponseEntity<String> response = studentService.updateStudentByStudentRegistrationNumber(new Student(
                123123,
                "New Name"
        ), mockStudent().getStudentRegistrationNumber());

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void givenRegisteredStudent_whenSearchingByHisName_shouldFindStudentSuccessfully() {

        List<Student> students = studentService.findByName(mockStudent().getName());

        assertEquals(mockStudent().getName(), students.get(0).getName());
    }

    @Test
    public void givenRegisteredStudent_whenSearchingByHisRegisterNumber_shouldFindStudentSuccessfully() {

        Student student = studentService.findByStudentRegistrationNumber(mockStudent().getStudentRegistrationNumber());

        assertEquals(mockStudent().getStudentRegistrationNumber(), student.getStudentRegistrationNumber());
    }

    @Test
    public void givenRegisteredStudent_whenDeletingHim_shouldDeleteStudentSuccessfully() {

        ResponseEntity<String> response = studentService.deleteStudentByStudentRegistrationNumber(mockStudent().getStudentRegistrationNumber());

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    private Student mockStudent() {
        return new Student(333000, "Student Name");
    }
}

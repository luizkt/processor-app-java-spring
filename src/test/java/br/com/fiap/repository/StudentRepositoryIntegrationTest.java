package br.com.fiap.repository;

import br.com.fiap.ProcessorApplication;
import br.com.fiap.config.ProcessorMySqlContainer;
import br.com.fiap.entity.Student;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProcessorApplication.class)
@ActiveProfiles({"integrationTest"})
public class StudentRepositoryIntegrationTest {

    @ClassRule
    public static MySQLContainer processorMySqlContainer = ProcessorMySqlContainer.getInstance();

    @Autowired
    StudentRepository studentRepository;

    @Before
    @Transactional("studentTransactionManager")
    public void insertStudents() {

        List<Student> students = new ArrayList<>();
        for (int index = 1; index < 6; index++) {
            students.add(new Student(index, "Name " + index));
        }
        studentRepository.saveAll(students);
    }

    @Test
    @Transactional
    public void shouldFindStudentByName() {
        List<Student> students = studentRepository.findByName("Name 1");

        assertEquals(1, students.size());
        assertEquals("Name 1", students.get(0).getName());
    }

    @Test
    @Transactional
    public void shouldFindByStudentRegistrationNumber() {
        Student student = studentRepository.findByStudentRegistrationNumber(1);

        assertTrue(student.getStudentRegistrationNumber() == 1);
        assertEquals("Name 1", student.getName());
    }

}

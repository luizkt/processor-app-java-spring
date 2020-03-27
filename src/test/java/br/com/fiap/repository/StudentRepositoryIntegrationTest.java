package br.com.fiap.repository;

import br.com.fiap.ProcessorApplication;
import br.com.fiap.config.ProcessorMySqlContainer;
import br.com.fiap.entity.Student;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@DataJpaTest
@RunWith(SpringRunner.class)
public class StudentRepositoryIntegrationTest {

    @ClassRule
    public static MySQLContainer processorMySqlContainer = ProcessorMySqlContainer.getInstance();

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    StudentRepository studentRepository;

    @Before
    @Transactional("studentTransactionManager")
    public void insertStudents() {

        List<Student> students = new ArrayList<>();
        for(int index = 1; index < 6; index++){
            students.add(new Student(index, "Name " + index));
        }
        studentRepository.saveAll(students);
    }


    @Test
    @Transactional
    public void shouldFindStudentByName() {
        List<Student> students = studentRepository.findByName("Name");

    }



}

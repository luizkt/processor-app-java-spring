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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProcessorApplication.class)
@ActiveProfiles({"integrationTest"})
public class TransactionServiceIntegrationTest {

    @ClassRule
    public static MySQLContainer processorMySqlContainer = ProcessorMySqlContainer.getInstance();

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    @Transactional("transactionManager")
    public void setUp() {

        Transaction transaction = mockTransaction();

        studentRepository.save(transaction.getStudent());
        transactionRepository.save(transaction);
    }

    @Test
    public void shouldAddTransactionSuccessfully() {
        Transaction transaction = new Transaction(
                2000,
                mockStudent(),
                333000,
                "4532",
                1.00,
                "Transaction description"
        );
        ResponseEntity<String> response = transactionService.add(transaction);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void shouldThrowExceptionForTransactionAlreadyExist() {
        ResponseEntity<String> response = transactionService.add(mockTransaction());

        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(response.getBody().contains("Transaction ID already exist"));
    }

    @Test
    public void shouldThrowExceptionForNonStudent() {
        Transaction transaction = new Transaction(
                1000,
                new Student(222000, "Name 3"),
                222000,
                "4532",
                1.00,
                "Transaction description"
        );

        ResponseEntity<String> response = transactionService.add(transaction);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertTrue(response.getBody().contains("Student registration number not found"));
    }

    @Test
    public void shouldFindAllTransactionsFromStudent() {
        ResponseEntity<List<Transaction>> response = transactionService.findAllTransactionsFromStudent(mockTransaction().getStudentRegistrationNumber());

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void shouldDeleteTheTransaction() {
        ResponseEntity<String> response = transactionService.deleteTransactionById(mockTransaction().getTransactionId());

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    private Student mockStudent() {
        return new Student(333000, "Student Name");
    }

    private Transaction mockTransaction() {
        return new Transaction(
                1000,
                mockStudent(),
                333000,
                "4532",
                1.00,
                "Transaction description"
        );
    }
}

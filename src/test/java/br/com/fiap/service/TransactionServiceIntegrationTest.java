package br.com.fiap.service;

import br.com.fiap.ProcessorApplication;
import br.com.fiap.config.ProcessorMySqlContainer;
import br.com.fiap.entity.ApplicationResponseBody;
import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.exception.BusinessException;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import br.com.fiap.service.impl.TransactionServiceImpl;
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
public class TransactionServiceIntegrationTest {

    @ClassRule
    public static MySQLContainer processorMySqlContainer = ProcessorMySqlContainer.getInstance();

    @Autowired
    private TransactionServiceImpl transactionService;

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
    public void givenNewTransaction_whenRegisteringIt_shouldAddTransactionSuccessfully() throws IOException {
        Transaction transaction = new Transaction(
                2000,
                mockStudent(),
                333000,
                "4532",
                1.00,
                "Transaction description"
        );
        ApplicationResponseBody response = transactionService.add(transaction);

        ObjectMapper mapper = new ObjectMapper();
        Transaction transactionResponse = mapper.convertValue(response.getData(), Transaction.class);

        assertEquals(transaction.getTransactionId(), transactionResponse.getTransactionId());
        assertEquals(transaction.getStudentRegistrationNumber(), transactionResponse.getStudentRegistrationNumber());
        assertEquals(transaction.getPanFinal(), transactionResponse.getPanFinal());
        assertEquals(transaction.getAmount(), transactionResponse.getAmount());
        assertEquals(transaction.getDescription(), transactionResponse.getDescription());
    }

    @Test(expected = BusinessException.class)
    public void givenNotRegisteredStudent_whenRegisteringNewTransactionForHim_shouldThrowExceptionForNonStudent() throws IOException {
        Transaction transaction = new Transaction(
                1000,
                new Student(222000, "Name 3"),
                222000,
                "4532",
                1.00,
                "Transaction description"
        );

        ApplicationResponseBody response = transactionService.add(transaction);

        ObjectMapper mapper = new ObjectMapper();

        assertTrue(response.getMessage().equals("Student registration number not found"));
    }

    @Test(expected = BusinessException.class)
    public void givenRegisteredTransaction_whenRegisteringIt_shouldThrowExceptionForTransactionAlreadyExist() throws IOException {
        ApplicationResponseBody response = transactionService.add(mockTransaction());

        assertTrue(response.getMessage().equals("Transaction ID already exist"));
    }

    @Test
    public void givenRegisteredStudent_whenSearchingForHisTransactions_shouldFindAllTransactionsFromStudent() throws IOException {
        ApplicationResponseBody response = transactionService.findAllTransactionsFromStudent(mockTransaction().getStudentRegistrationNumber());

        ObjectMapper mapper = new ObjectMapper();
        List<Transaction> transactionResponse = Arrays.asList(mapper.convertValue(response.getData(), Transaction[].class));

        assertEquals(mockTransaction().getTransactionId(), transactionResponse.get(0).getTransactionId());
        assertEquals(mockTransaction().getStudentRegistrationNumber(), transactionResponse.get(0).getStudentRegistrationNumber());
        assertEquals(mockTransaction().getPanFinal(), transactionResponse.get(0).getPanFinal());
        assertEquals(mockTransaction().getAmount(), transactionResponse.get(0).getAmount());
        assertEquals(mockTransaction().getDescription(), transactionResponse.get(0).getDescription());
    }

    @Test
    public void givenRegisteredTransaction_whenDeletingIt_shouldDeleteTheTransaction() throws IOException {
        ApplicationResponseBody response = transactionService.deleteTransactionById(mockTransaction().getTransactionId());

        ObjectMapper mapper = new ObjectMapper();
        Transaction transactionResponse = mapper.convertValue(response.getData(), Transaction.class);

        assertEquals(mockTransaction().getTransactionId(), transactionResponse.getTransactionId());
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

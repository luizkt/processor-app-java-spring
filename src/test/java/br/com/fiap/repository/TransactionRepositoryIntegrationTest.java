package br.com.fiap.repository;

import br.com.fiap.ProcessorApplication;
import br.com.fiap.config.ProcessorMySqlContainer;
import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProcessorApplication.class)
@ActiveProfiles({"integrationTest"})
public class TransactionRepositoryIntegrationTest {

    @ClassRule
    public static MySQLContainer processorMySqlContainer = ProcessorMySqlContainer.getInstance();

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Before
    @Transactional("transactionTransactionManager")
    public void insertTransactions() {

        List<Student> students = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        for (int studentIndex = 1; studentIndex < 6; studentIndex++) {
            students.add(new Student(studentIndex, "Name " + studentIndex));
            for(int transactionIndex = 10; transactionIndex < 21; transactionIndex = transactionIndex + 10 ) {
                transactions.add(new Transaction(transactionIndex + studentIndex, students.get(studentIndex-1), students.get(studentIndex-1).getStudentRegistrationNumber(), "123" + transactionIndex, 0.99 + transactionIndex, "transaction " + transactionIndex + studentIndex));
            }
        }
        studentRepository.saveAll(students);
        transactionRepository.saveAll(transactions);
    }

    @Test
    @Transactional
    public void givenRegisteredStudent_whenSearchingHisTransactions_shouldFindAllTransactionsFromStudent() {
        List<Transaction> transactions = transactionRepository.findAllTransactionsFromStudent(new Student(1, "Name 1"));

        assertTrue(transactions.get(0).getTransactionId() == 11);
        assertTrue(transactions.get(1).getTransactionId() == 21);
    }

    @Test
    @Transactional
    public void givenRegisteredTransaction_whenSearchingById_shouldFindTransactionByTransactionId() {
        Transaction transaction = transactionRepository.findTransactionByTransactionId(11);

        assertTrue(transaction.getTransactionId() == 11);
    }
}

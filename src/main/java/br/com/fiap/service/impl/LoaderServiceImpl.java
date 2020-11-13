package br.com.fiap.service.impl;

import br.com.fiap.entity.ResponseBody;
import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import br.com.fiap.service.LoaderService;
import br.com.fiap.utils.ErrorResponse;
import br.com.fiap.utils.NameFormatter;
import br.com.fiap.utils.SuccessResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class LoaderServiceImpl implements LoaderService {

    private final StudentRepository studentRepository;
    private final TransactionRepository transactionRepository;

    public LoaderServiceImpl(StudentRepository studentRepository, TransactionRepository transactionRepository) {
        this.studentRepository = studentRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional()
    @Override
    public ResponseEntity<String> loadFromCsv() throws JsonProcessingException {
        try {
            log.info("Reading students from file");
            List<Student> students = csvReaderStudent();
            log.info("Student's file read successfully");

            log.info("Reading transactions from file");
            List<Transaction> transactions = csvReaderTransaction(students);
            log.info("Transaction's file read successfully");

            log.info("Adding all student's to the database");
            studentRepository.saveAll(students);
            log.info("Added all student's successfully");

            log.info("Adding all transaction's to the database");
            transactionRepository.saveAll(transactions);
            log.info("Added all transaction's successfully");

            return SuccessResponse.build(new ResponseBody("Added all the students and transactions successfully", null), HttpStatus.CREATED);
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

    private List<Student> csvReaderStudent() throws IOException {
        List<Student> students = new ArrayList<>();

        BufferedReader csvReader = new BufferedReader(new FileReader("./files/students_list.csv"));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            Student student = new Student(Integer.parseInt(data[0]), NameFormatter.capitalizeName(data[1]));

            students.add(student);
        }
        csvReader.close();
        return students;
    }

    private List<Transaction> csvReaderTransaction(List<Student> students) throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        BufferedReader csvReader = new BufferedReader(new FileReader("./files/transactions_list.csv"));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            Transaction transaction = new Transaction(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], Double.parseDouble(data[3]), data[4]);

            transaction.setStudent(
                    students.stream()
                            .filter(student -> transaction.getStudentRegistrationNumber().equals(student.getStudentRegistrationNumber()))
                            .findAny()
                            .orElse(null));

            transactions.add(transaction);
        }
        csvReader.close();
        return transactions;
    }
}

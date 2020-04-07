package br.com.fiap.service;

import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import br.com.fiap.utils.NameFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoaderService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional()
    public ResponseEntity<String> loadFromCsv() {

        try {

            List<Student> students = csvReaderStudent();
            List<Transaction> transactions = csvReaderTransaction();

            studentRepository.saveAll(students);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Added all the students successfully\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"An error has occurred\", \"exception\":" + e.getMessage() + "}";

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }
    }

    private List<Student> csvReaderStudent() throws IOException {

        List<Student> students = new ArrayList<>();

        BufferedReader csvReader = new BufferedReader(new FileReader("./files/lista_alunos.csv"));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            Student student = new Student(Integer.parseInt(data[0]), NameFormatter.capitalizeName(data[1]));

            students.add(student);
        }
        csvReader.close();
        return students;
    }

    private List<Transaction> csvReaderTransaction() throws IOException {

        List<Transaction> transactions = new ArrayList<>();

        BufferedReader csvReader = new BufferedReader(new FileReader("./files/lista_transacoes.csv"));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            Transaction transaction = new Transaction(Integer transactionId, Integer studentRegistrationNumber, String panFinal, Double amount, String description);

            transactions.add(transaction);
        }
        csvReader.close();
        return transactions;
    }
}
}

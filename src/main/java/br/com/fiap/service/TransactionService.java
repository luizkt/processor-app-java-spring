package br.com.fiap.service;

import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import br.com.fiap.utils.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@Log4j2
public class TransactionService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public ResponseEntity<String> add(Transaction transaction) {
        try {
            if (transaction.getStudentRegistrationNumber() == null)
                throw new Exception("\"Student registration number is required\"");

            transaction.setStudent(studentRepository.findByStudentRegistrationNumber(transaction.getStudentRegistrationNumber()));

            if (transaction.getStudent() == null)
                throw new Exception("\"Student registration number not found\"");

            if (transactionRepository.existsById(transaction.getTransactionId()))
                throw new Exception("\"Transaction ID already exist\"");

            ObjectMapper mapper = new ObjectMapper();

            log.info("Adding transaction: " + mapper.writeValueAsString(transaction));

            transactionRepository.save(transaction);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Added the transaction successfully\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<Transaction>> findAllTransactionsFromStudent(Integer studentRegistrationNumber) {

        log.info("Searching for transactions from student registration number: " + studentRegistrationNumber);

        Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

        List<Transaction> transactions = transactionRepository.findAllTransactionsFromStudent(student);

        transactions.forEach(transaction -> transaction.setStudentRegistrationNumber(transaction.getStudent().getStudentRegistrationNumber()));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        return new ResponseEntity<>(transactions, headers, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteTransactionById(Integer transactionId) {
        try {

            Transaction transaction = transactionRepository.findTransactionByTransactionId(transactionId);

            ObjectMapper mapper = new ObjectMapper();

            log.info("Deleting transaction: " + mapper.writeValueAsString(transaction));

            transactionRepository.deleteById(transaction.getTransactionId());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Deleted the transaction successfully\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ErrorResponse.build(e);
        }

    }
}

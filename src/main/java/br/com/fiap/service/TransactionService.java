package br.com.fiap.service;

import br.com.fiap.entity.ResponseBody;
import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import br.com.fiap.utils.ErrorResponse;
import br.com.fiap.utils.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
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
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Student registration number is required");

            transaction.setStudent(studentRepository.findByStudentRegistrationNumber(transaction.getStudentRegistrationNumber()));

            if (transaction.getStudent() == null)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Student registration number not found");

            if (transactionRepository.existsById(transaction.getTransactionId()))
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Transaction ID already exist");

            ObjectMapper mapper = new ObjectMapper();

            log.info("Adding transaction: " + mapper.writeValueAsString(transaction));

            transactionRepository.save(transaction);

            return SuccessResponse.build(new ResponseBody("Added the transaction successfully", transaction.toJsonString()), HttpStatus.CREATED);
        } catch (HttpClientErrorException e) {
            return ErrorResponse.build(e, e.getStatusCode());
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<String> findAllTransactionsFromStudent(Integer studentRegistrationNumber) {
        try {
            log.info("Searching for transactions from student registration number: " + studentRegistrationNumber);

            Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            List<Transaction> transactions = transactionRepository.findAllTransactionsFromStudent(student);

            List<String> transactionJsonString = new ArrayList<>();
            transactions.forEach(transaction -> {
                transaction.setStudentRegistrationNumber(transaction.getStudent().getStudentRegistrationNumber());
                transactionJsonString.add(transaction.toJsonString());
            });

            return SuccessResponse.build(new ResponseBody("Search for the student's transaction successfully", transactionJsonString), HttpStatus.OK);
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteTransactionById(Integer transactionId) {
        try {
            Transaction transaction = transactionRepository.findTransactionByTransactionId(transactionId);

            ObjectMapper mapper = new ObjectMapper();

            log.info("Deleting transaction: " + mapper.writeValueAsString(transaction));

            transactionRepository.deleteById(transaction.getTransactionId());

            return SuccessResponse.build(new ResponseBody("Deleted the transaction successfully", transaction.toJsonString()), HttpStatus.OK);
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }
}

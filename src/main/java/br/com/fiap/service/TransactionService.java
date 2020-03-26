package br.com.fiap.service;

import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> add(@Valid @RequestBody Transaction transaction) {
        try {
            if (transaction.getStudentRegistrationNumber() == null)
                throw new Exception("\"Student registration number is required\"");

            transaction.setStudent(studentRepository.findByStudentRegistrationNumber(transaction.getStudentRegistrationNumber()));

            if (transaction.getStudent() == null)
                throw new Exception("\"Student registration number not found\"");

            if (transactionRepository.existsById(transaction.getTransactionId()))
                throw new Exception("\"Transaction ID already exist\"");

            transactionRepository.save(transaction);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Added the transaction successfully\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"An error has occurred\", \"exception\":" + e.getMessage() + "}";

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    @RequestMapping(path = "student/{studentRegistrationNumber}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Transaction>> findAllTransactionsFromStudent(@PathVariable Integer studentRegistrationNumber) {

        Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

        List<Transaction> transactions = transactionRepository.findAllTransactionsFromStudent(student);

        transactions.forEach(transaction -> transaction.setStudentRegistrationNumber(transaction.getStudent().getStudentRegistrationNumber()));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        return new ResponseEntity<>(transactions, headers, HttpStatus.OK);

    }
}

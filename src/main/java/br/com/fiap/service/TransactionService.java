package br.com.fiap.service;

import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.model.TransactionJson;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import br.com.fiap.service.exception.TransactionServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<String> add (@Valid @RequestBody Map<String, Object> payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            TransactionJson transactionJson = mapper.convertValue(payload, TransactionJson.class);
            Transaction transaction = new Transaction();

            Student student = studentRepository.findByStudentRegistrationNumber(transactionJson.getStudentRegistrationNumber());
            if(student == null) {
                throw new TransactionServiceException("\"Student registration number not found\"");
            }

            if(transactionRepository.existsById(transactionJson.getTransactionId())) {
                throw new TransactionServiceException("\"Transaction ID already exist\"");
            }

            transaction.setTransactionId(transactionJson.getTransactionId());
            transaction.setStudent(student);
            transaction.setPanFinal(transactionJson.getPanFinal());
            transaction.setAmount(transactionJson.getAmount());
            transaction.setDescription(transactionJson.getDescription());

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
    public ResponseEntity<List<TransactionJson>> findAllTransactionsFromStudent(@PathVariable Integer studentRegistrationNumber) {

        Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

        List<Transaction> transactions = transactionRepository.findAllTransactionsFromStudent(student);

        List<TransactionJson> transactionsJson = new ArrayList<>();
        for(Transaction transaction : transactions) {
            TransactionJson transactionJson = new TransactionJson();

            transactionJson.setTransactionId(transaction.getTransactionId());
            transactionJson.setStudentRegistrationNumber(transaction.getStudent().getStudentRegistrationNumber());
            transactionJson.setPanFinal(transaction.getPanFinal());
            transactionJson.setAmount(transaction.getAmount());
            transactionJson.setDescription(transaction.getDescription());

            transactionsJson.add(transactionJson);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        return new ResponseEntity<>(transactionsJson, headers, HttpStatus.OK);

    }
}

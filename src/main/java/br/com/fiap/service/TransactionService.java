package br.com.fiap.service;

import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @RequestMapping(path = "/add", method = RequestMethod.POST, produces="application/json", consumes="application/json")
    @ResponseBody
    @ApiOperation(value = "Create new transaction for the student")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Add new transaction"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
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
    @RequestMapping(path = "/student/{studentRegistrationNumber}", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    @ApiOperation(value = "Find all transactions by student registration number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get all transactions from the student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<List<Transaction>> findAllTransactionsFromStudent(@PathVariable Integer studentRegistrationNumber) {

        Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

        List<Transaction> transactions = transactionRepository.findAllTransactionsFromStudent(student);

        transactions.forEach(transaction -> transaction.setStudentRegistrationNumber(transaction.getStudent().getStudentRegistrationNumber()));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        return new ResponseEntity<>(transactions, headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(path = "/{transactionId}", method = RequestMethod.DELETE, produces="application/json")
    @ResponseBody
    @ApiOperation(value = "Delete the transaction by transaction ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete the transaction"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<String> deleteTransactionById(@PathVariable Integer transactionId) {

        try {

            Transaction transaction = transactionRepository.findTransactionByTransactionId(transactionId);

            studentRepository.deleteById(transaction.getTransactionId());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Deleted the transaction successfully\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"An error has occurred\", \"exception\":" + e.getMessage() + "}";

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }

    }
}

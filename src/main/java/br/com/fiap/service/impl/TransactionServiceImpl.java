package br.com.fiap.service.impl;

import br.com.fiap.entity.ResponseBody;
import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import br.com.fiap.service.TransactionService;
import br.com.fiap.utils.ErrorResponse;
import br.com.fiap.utils.SuccessResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;

@Service
@Log4j2
public class TransactionServiceImpl implements TransactionService {

    private final StudentRepository studentRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(StudentRepository studentRepository, TransactionRepository transactionRepository) {
        this.studentRepository = studentRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<String> add(Transaction transaction) throws JsonProcessingException {
        try {
            log.info("Adding transaction");

            if (transaction.getStudentRegistrationNumber() == null)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Student registration number is required");

            transaction.setStudent(studentRepository.findByStudentRegistrationNumber(transaction.getStudentRegistrationNumber()));

            if (transaction.getStudent() == null)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Student registration number not found");

            if (transactionRepository.existsById(transaction.getTransactionId()))
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Transaction ID already exist");

            ObjectMapper mapper = new ObjectMapper();

            log.debug("Adding transaction: " + mapper.writeValueAsString(transaction));

            transactionRepository.save(transaction);

            return SuccessResponse.build(new ResponseBody("Added the transaction successfully", transaction), HttpStatus.CREATED);
        } catch (HttpClientErrorException e) {
            return ErrorResponse.build(e, e.getStatusCode());
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<String> findAllTransactionsFromStudent(Integer studentRegistrationNumber) throws JsonProcessingException {
        try {
            log.info("Searching transactions");
            log.debug("Searching transactions from student registration number [" + studentRegistrationNumber + "]");

            Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            List<Transaction> transactions = transactionRepository.findAllTransactionsFromStudent(student);

            if(transactions.size() == 0)
                return SuccessResponse.build(new ResponseBody("No transactions found"), HttpStatus.NO_CONTENT);

            transactions.forEach(transaction -> transaction.setStudentRegistrationNumber(transaction.getStudent().getStudentRegistrationNumber()));

            return SuccessResponse.build(new ResponseBody("Search for the student's transaction successfully", transactions), HttpStatus.OK);
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteTransactionById(Integer transactionId) throws JsonProcessingException {
        try {
            log.info("Deleting transaction");

            Transaction transaction = transactionRepository.findTransactionByTransactionId(transactionId);

            ObjectMapper mapper = new ObjectMapper();

            log.debug("Deleting transaction: " + mapper.writeValueAsString(transaction));

            transactionRepository.deleteById(transaction.getTransactionId());

            return SuccessResponse.build(new ResponseBody("Deleted the transaction successfully", transaction), HttpStatus.OK);
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }
}
package br.com.fiap.service.impl;

import br.com.fiap.entity.ApplicationResponseBody;
import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.exception.BusinessException;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.repository.TransactionRepository;
import br.com.fiap.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static br.com.fiap.utils.Message.*;

@Service
@Log4j2
public class TransactionServiceImpl implements TransactionService {

    private final StudentRepository studentRepository;
    private final TransactionRepository transactionRepository;

    private final String logPrefix = "[Service] [Transaction]";

    public TransactionServiceImpl(StudentRepository studentRepository, TransactionRepository transactionRepository) {
        this.studentRepository = studentRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    @Override
    public ApplicationResponseBody add(Transaction transaction) throws JsonProcessingException {
        try {
            log.info(logPrefix + " Adding transaction");

            if (transaction.getStudentRegistrationNumber() == null)
                throw new BusinessException(HttpStatus.BAD_REQUEST, STUDENT_REGISTRATION_NUMBER_REQUIRED);

            transaction.setStudent(studentRepository.findByStudentRegistrationNumber(transaction.getStudentRegistrationNumber()));

            if (transaction.getStudent() == null)
                throw new BusinessException(HttpStatus.BAD_REQUEST, STUDENT_DOES_NOT_EXIST);

            if (transactionRepository.existsById(transaction.getTransactionId()))
                throw new BusinessException(HttpStatus.BAD_REQUEST, TRANSACTION_ALREADY_REGISTERED);

            ObjectMapper mapper = new ObjectMapper();

            log.debug(logPrefix + " Adding transaction: " + mapper.writeValueAsString(transaction));

            transactionRepository.save(transaction);

            return new ApplicationResponseBody(TRANSACTION_ADDED_SUCCESSFULLY, transaction);
        } catch (BusinessException e) {
            log.error(logPrefix + " " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(logPrefix + " " + e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ApplicationResponseBody findAllTransactionsFromStudent(Integer studentRegistrationNumber) throws JsonProcessingException {
        try {
            log.info("Searching transactions");
            log.debug("Searching transactions from student registration number [" + studentRegistrationNumber + "]");

            Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            List<Transaction> transactions = transactionRepository.findAllTransactionsFromStudent(student);

            if(transactions.size() == 0)
                return new ApplicationResponseBody(TRANSACTION_NOT_FOUND);

            transactions.forEach(transaction -> transaction.setStudentRegistrationNumber(transaction.getStudent().getStudentRegistrationNumber()));

            return new ApplicationResponseBody(FOUND_TRANSACTION_FROM_STUDENT, transactions);
        } catch (Exception e) {
            log.error(logPrefix + " " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    @Override
    public ApplicationResponseBody deleteTransactionById(Integer transactionId) throws JsonProcessingException {
        try {
            log.info("Deleting transaction");

            Transaction transaction = transactionRepository.findTransactionByTransactionId(transactionId);

            ObjectMapper mapper = new ObjectMapper();

            log.debug("Deleting transaction: " + mapper.writeValueAsString(transaction));

            transactionRepository.deleteById(transaction.getTransactionId());

            return new ApplicationResponseBody(TRANSACTION_DELETED_SUCCESSFULLY, transaction);
        } catch (Exception e) {
            log.error(logPrefix + " " + e.getMessage());
            throw e;
        }
    }
}

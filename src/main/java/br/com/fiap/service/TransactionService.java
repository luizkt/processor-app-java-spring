package br.com.fiap.service;

import br.com.fiap.entity.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<String> add(Transaction transaction) throws JsonProcessingException;
    ResponseEntity<String> findAllTransactionsFromStudent(Integer studentRegistrationNumber) throws JsonProcessingException;
    ResponseEntity<String> deleteTransactionById(Integer transactionId) throws JsonProcessingException;
}

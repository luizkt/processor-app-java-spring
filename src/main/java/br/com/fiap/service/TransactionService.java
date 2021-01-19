package br.com.fiap.service;

import br.com.fiap.entity.ApplicationResponseBody;
import br.com.fiap.entity.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface TransactionService {
    ApplicationResponseBody add(Transaction transaction) throws JsonProcessingException;
    ApplicationResponseBody findAllTransactionsFromStudent(Integer studentRegistrationNumber) throws JsonProcessingException;
    ApplicationResponseBody deleteTransactionById(Integer transactionId) throws JsonProcessingException;
}

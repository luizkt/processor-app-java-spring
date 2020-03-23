package br.com.fiap.service.exception;

public class TransactionServiceException extends Exception {

    public TransactionServiceException(String errorMessage) {
        super(errorMessage);
    }

}

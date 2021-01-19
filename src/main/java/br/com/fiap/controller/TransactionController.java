package br.com.fiap.controller;

import br.com.fiap.entity.ApplicationResponseBody;
import br.com.fiap.entity.Transaction;
import br.com.fiap.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(path = "/transactions", method = RequestMethod.POST, produces="application/json", consumes="application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new transaction for the student")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Add new transaction"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ApplicationResponseBody add(@Valid @RequestBody Transaction transaction) throws JsonProcessingException {
        return transactionService.add(transaction);
    }

    @RequestMapping(path = "/transactions/{transactionId}", method = RequestMethod.DELETE, produces="application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete the transaction by transaction ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete the transaction"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ApplicationResponseBody deleteTransactionById(@PathVariable Integer transactionId) throws JsonProcessingException {
        return transactionService.deleteTransactionById(transactionId);
    }
}

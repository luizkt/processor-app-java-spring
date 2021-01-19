package br.com.fiap.controller;

import br.com.fiap.entity.ApplicationResponseBody;
import br.com.fiap.service.LoaderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class LoaderController {

    private final LoaderService loaderService;

    public LoaderController(LoaderService loaderService) {
        this.loaderService = loaderService;
    }

    @RequestMapping(value = "/loader/load_from_csv", method = RequestMethod.POST, produces="application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add students and transactions from csv files")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Add students and transactions from csv files"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ApplicationResponseBody loadFromCsv() throws IOException {
        return loaderService.loadFromCsv();
    }
}

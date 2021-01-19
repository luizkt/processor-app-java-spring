package br.com.fiap.controller;

import br.com.fiap.entity.ApplicationResponseBody;
import br.com.fiap.entity.Student;
import br.com.fiap.service.StudentService;
import br.com.fiap.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class StudentController {

    private final StudentService studentService;
    private final TransactionService transactionService;

    public StudentController(StudentService studentService, TransactionService transactionService){
        this.studentService = studentService;
        this.transactionService = transactionService;
    }

    @RequestMapping(path = "/students", method = RequestMethod.POST, produces="application/json", consumes="application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new student")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Add new student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ApplicationResponseBody add(@Valid @RequestBody Student student) throws JsonProcessingException {
        return studentService.add(student);
    }

    @RequestMapping(path = "/students/{studentRegistrationNumber}", method = RequestMethod.PATCH, produces="application/json", consumes="application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update the student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Update the student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ApplicationResponseBody updateStudentByStudentRegistrationNumber(@RequestBody Student studentUpdate,
                                                                           @PathVariable Integer studentRegistrationNumber) throws JsonProcessingException {
        return studentService.updateStudentByStudentRegistrationNumber(studentUpdate,studentRegistrationNumber);
    }

    @RequestMapping(path = "/students/{studentRegistrationNumber}", method = RequestMethod.DELETE, produces="application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete the student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete the student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ApplicationResponseBody deleteStudentByStudentRegistrationNumber(@PathVariable Integer studentRegistrationNumber) throws JsonProcessingException {
        return studentService.deleteStudentByStudentRegistrationNumber(studentRegistrationNumber);
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Search students matching name pattern")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found student by name"),
            @ApiResponse(code = 204, message = "No student found by name"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ApplicationResponseBody findByName(
            @RequestParam String name,
            HttpServletResponse response
    ) throws JsonProcessingException {
        ApplicationResponseBody applicationResponse = studentService.findByName(name);

        if(applicationResponse.getData() != null)
            response.setStatus(HttpStatus.OK.value());
        else
            response.setStatus(HttpStatus.NO_CONTENT.value());

        return applicationResponse;
    }

    @RequestMapping(value = "/students/{studentRegistrationNumber}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Search for student student by registration number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search for student by registration number"),
            @ApiResponse(code = 204, message = "No student found by registration number"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ApplicationResponseBody findByStudentRegistrationNumber(
            @PathVariable Integer studentRegistrationNumber,
            HttpServletResponse response
    ) throws JsonProcessingException {
        ApplicationResponseBody applicationResponse = studentService.findByStudentRegistrationNumber(studentRegistrationNumber);

        if(applicationResponse.getData() != null)
            response.setStatus(HttpStatus.OK.value());
        else
            response.setStatus(HttpStatus.NO_CONTENT.value());

        return applicationResponse;
    }

    @RequestMapping(path = "/students/{studentRegistrationNumber}/transactions", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    @ApiOperation(value = "Find all transactions by student registration number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found all transactions from the student"),
            @ApiResponse(code = 204, message = "No transactions found from the student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ApplicationResponseBody findAllTransactionsFromStudent(
            HttpServletResponse response,
            @PathVariable Integer studentRegistrationNumber
    ) throws JsonProcessingException {
        ApplicationResponseBody applicationResponse = transactionService.findAllTransactionsFromStudent(studentRegistrationNumber);

        if(applicationResponse.getData() != null)
            response.setStatus(HttpStatus.OK.value());
        else
            response.setStatus(HttpStatus.NO_CONTENT.value());

        return applicationResponse;
    }

}

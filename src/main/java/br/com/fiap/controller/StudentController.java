package br.com.fiap.controller;

import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import br.com.fiap.service.StudentService;
import br.com.fiap.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @ApiOperation(value = "Create new student")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Add new student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<String> add(@Valid @RequestBody Student student) {
        return studentService.add(student);
    }

    @RequestMapping(path = "/students/{studentRegistrationNumber}", method = RequestMethod.PATCH, produces="application/json", consumes="application/json")
    @ResponseBody
    @ApiOperation(value = "Update the student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Update the student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<String> updateStudentByStudentRegistrationNumber(@RequestBody Student studentUpdate,
                                                                           @PathVariable Integer studentRegistrationNumber) {
        return studentService.updateStudentByStudentRegistrationNumber(studentUpdate,studentRegistrationNumber);
    }

    @RequestMapping(path = "/students/{studentRegistrationNumber}", method = RequestMethod.DELETE, produces="application/json")
    @ResponseBody
    @ApiOperation(value = "Delete the student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete the student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<String> deleteStudentByStudentRegistrationNumber(@PathVariable Integer studentRegistrationNumber) {
        return studentService.deleteStudentByStudentRegistrationNumber(studentRegistrationNumber);
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Search students matching name pattern")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search for student by name"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public List<Student> findByName(@RequestParam String name) {
        return studentService.findByName(name);
    }

    @RequestMapping(value = "/students/{studentRegistrationNumber}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Search for student studentby registration number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search for student by registration number"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public Student findByStudentRegistrationNumber(@PathVariable Integer studentRegistrationNumber) {
        return studentService.findByStudentRegistrationNumber(studentRegistrationNumber);
    }

    @RequestMapping(path = "/students/{studentRegistrationNumber}/transactions", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    @ApiOperation(value = "Find all transactions by student registration number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get all transactions from the student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<List<Transaction>> findAllTransactionsFromStudent(@PathVariable Integer studentRegistrationNumber) {
        return transactionService.findAllTransactionsFromStudent(studentRegistrationNumber);
    }

}

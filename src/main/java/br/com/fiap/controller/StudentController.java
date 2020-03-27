package br.com.fiap.controller;

import br.com.fiap.entity.Student;
import br.com.fiap.service.StudentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping(path = "/add", method = RequestMethod.POST, produces="application/json", consumes="application/json")
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

    @RequestMapping(value = "/load_from_csv", method = RequestMethod.POST, produces="application/json")
    @ResponseBody
    @ApiOperation(value = "Create new students from CSV file")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Add new students"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<String> loadFromCsv() {
        return studentService.loadFromCsv();
    }

    @RequestMapping(path = "/{studentRegistrationNumber}", method = RequestMethod.PATCH, produces="application/json", consumes="application/json")
    @ResponseBody
    @ApiOperation(value = "Update the student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Update the student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<String> updateStudentByStudentRegistrationNumber(@RequestBody Student studentUpdate,
                                                                           @PathVariable("studentRegistrationNumber") Integer studentRegistrationNumber) {
        return studentService.updateStudentByStudentRegistrationNumber(studentUpdate,studentRegistrationNumber);
    }

    @RequestMapping(path = "/{studentRegistrationNumber}", method = RequestMethod.DELETE, produces="application/json")
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

    @RequestMapping(path = "/all", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    @ApiOperation(value = "Get all registered students")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get all registered students"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public Iterable<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Search for student by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search for student by name"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public List<Student> findByName(@PathVariable String name) {
        return studentService.findByName(name);
    }

    @RequestMapping(value = "/studentRegistrationNumber/{studentRegistrationNumber}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Search for student by registration number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search for student by registration number"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public Student findByStudentRegistrationNumber(@PathVariable Integer studentRegistrationNumber) {
        return studentService.findByStudentRegistrationNumber(studentRegistrationNumber);
    }

}

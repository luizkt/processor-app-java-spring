package br.com.fiap.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import br.com.fiap.entity.Student;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.utils.NameFormatter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/student")
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    @RequestMapping(path = "/add", method = RequestMethod.POST, produces="application/json", consumes="application/json")
    @ResponseBody
    @ApiOperation(value = "Create new student")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Add new student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<String> add(@Valid @RequestBody Student student) {

        try {
            student.setName(NameFormatter.capitalizeName(student.getName()));
            studentRepository.save(student);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Added the student successfully\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"An error has occurred\", \"exception\":" + e.getMessage() + "}";

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional()
    @RequestMapping(value = "/load_from_csv", method = RequestMethod.POST, produces="application/json")
    @ResponseBody
    @ApiOperation(value = "Create new students from CSV file")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Add new students"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<String> loadFromCsv() {

        List<Student> students = new ArrayList<>();

        try {

            BufferedReader csvReader = new BufferedReader(new FileReader("./files/lista_alunos.csv"));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(";");
                Student student = new Student();

                student.setName(NameFormatter.capitalizeName(data[0]));
                student.setStudentRegistrationNumber(Integer.parseInt(data[1]));

                students.add(student);
            }
            csvReader.close();

            studentRepository.saveAll(students);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Added all the students successfully\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"An error has occurred\", \"exception\":" + e.getMessage() + "}";

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
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

        try {

            Student studentDatabase = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            studentDatabase.setName(studentUpdate.getName() == null || studentUpdate.getName().isEmpty()
                    ? NameFormatter.capitalizeName(studentDatabase.getName())
                    : NameFormatter.capitalizeName(studentUpdate.getName()));

            studentRepository.save(studentDatabase);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Updated the student successfully\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"An error has occurred\", \"exception\":" + e.getMessage() + "}";

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
    @RequestMapping(path = "/{studentRegistrationNumber}", method = RequestMethod.DELETE, produces="application/json")
    @ResponseBody
    @ApiOperation(value = "Delete the student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete the student"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public ResponseEntity<String> deleteStudentByStudentRegistrationNumber(@PathVariable Integer studentRegistrationNumber) {

        try {

            Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            studentRepository.deleteById(student.getStudentRegistrationNumber());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Deleted the student successfully\"}";

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"An error has occurred\", \"exception\":" + e.getMessage() + "}";

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional(readOnly = true)
    @RequestMapping(path = "/all", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    @ApiOperation(value = "Get all registered students")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get all registered students"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Search for student by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search for student by name"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public List<Student> findByName(@PathVariable String name) {
        return studentRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/studentRegistrationNumber/{studentRegistrationNumber}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Search for student by registration number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search for student by registration number"),
            @ApiResponse(code = 400, message = "Some field have wrong information"),
            @ApiResponse(code = 500, message = "Some error occurred"),
    })
    public Student findByStudentRegistrationNumber(@PathVariable Integer studentRegistrationNumber) {
        return studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);
    }

}

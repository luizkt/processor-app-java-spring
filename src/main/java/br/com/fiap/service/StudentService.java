package br.com.fiap.service;

import java.util.List;

import br.com.fiap.entity.ResponseBody;
import br.com.fiap.entity.Student;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.utils.ErrorResponse;
import br.com.fiap.utils.NameFormatter;
import br.com.fiap.utils.SuccessResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Log4j2
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public ResponseEntity<String> add(Student student) throws JsonProcessingException {

        try {
            student.setName(NameFormatter.capitalizeName(student.getName()));

            if (studentRepository.existsById(student.getStudentRegistrationNumber()))
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Student registration number already exist");

            ObjectMapper mapper = new ObjectMapper();

            log.info("Adding student: " + mapper.writeValueAsString(student));

            studentRepository.save(student);

            return SuccessResponse.build(new ResponseBody("Added the student successfully", student), HttpStatus.CREATED);
        } catch (HttpClientErrorException e) {
            return ErrorResponse.build(e, e.getStatusCode());
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

    @Transactional
    public ResponseEntity<String> updateStudentByStudentRegistrationNumber(Student studentUpdate, Integer studentRegistrationNumber) throws JsonProcessingException {
        try {
            Student studentDatabase = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            if(studentDatabase == null)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Student registration number doesn't exist");

            studentDatabase.setName(studentUpdate.getName() == null || studentUpdate.getName().isEmpty()
                    ? NameFormatter.capitalizeName(studentDatabase.getName())
                    : NameFormatter.capitalizeName(studentUpdate.getName()));

            ObjectMapper mapper = new ObjectMapper();

            log.info("Updating student: " + mapper.writeValueAsString(studentDatabase));

            studentRepository.save(studentDatabase);

            return SuccessResponse.build(new ResponseBody("Updated the student successfully", studentDatabase), HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return ErrorResponse.build(e, e.getStatusCode());
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteStudentByStudentRegistrationNumber(Integer studentRegistrationNumber) throws JsonProcessingException {
        try {

            Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            if(student == null)
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Student registration number doesn't exist");

            ObjectMapper mapper = new ObjectMapper();

            log.info("Deleting student: " + mapper.writeValueAsString(student));

            studentRepository.deleteById(student.getStudentRegistrationNumber());

            return SuccessResponse.build(new ResponseBody("Added the student successfully", student), HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return ErrorResponse.build(e, e.getStatusCode());
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<String> findByName(String name) throws JsonProcessingException {
        try{
            log.info("Searching for student name: " + name);
            List<Student> students = studentRepository.findByName(name);

            if(students.size() == 0)
                return SuccessResponse.build(new ResponseBody("No student found", null), HttpStatus.NO_CONTENT);

            ObjectMapper mapper = new ObjectMapper();

            return SuccessResponse.build(new ResponseBody("Search for student's name successfully", students), HttpStatus.OK);
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<String> findByStudentRegistrationNumber(Integer studentRegistrationNumber) throws JsonProcessingException {
        try {
            log.info("Searching for student registration number: " + studentRegistrationNumber);
            Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            if(student == null)
                return SuccessResponse.build(new ResponseBody("No student found", null), HttpStatus.NO_CONTENT);

            ObjectMapper mapper = new ObjectMapper();

            return SuccessResponse.build(new ResponseBody("Search for student's registration number successfully", student), HttpStatus.OK);
        } catch (Exception e) {
            return ErrorResponse.build(e);
        }
    }

}

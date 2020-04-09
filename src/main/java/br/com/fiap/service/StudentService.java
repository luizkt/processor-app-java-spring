package br.com.fiap.service;

import java.util.List;

import br.com.fiap.entity.Student;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.utils.ErrorResponse;
import br.com.fiap.utils.NameFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public ResponseEntity<String> add(Student student) {

        try {
            student.setName(NameFormatter.capitalizeName(student.getName()));

            if (studentRepository.existsById(student.getStudentRegistrationNumber()))
                throw new Exception("\"Student registration number already exist\"");

            ObjectMapper mapper = new ObjectMapper();

            log.info("Adding student: " + mapper.writeValueAsString(student));

            studentRepository.save(student);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Added the student successfully\"}";

            log.info(body);

            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            return ErrorResponse.build(e);
        }

    }

    @Transactional
    public ResponseEntity<String> updateStudentByStudentRegistrationNumber(Student studentUpdate, Integer studentRegistrationNumber) {
        try {
            Student studentDatabase = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            if(studentDatabase == null)
                throw new Exception("\"Student registration number doesn't exist\"");

            studentDatabase.setName(studentUpdate.getName() == null || studentUpdate.getName().isEmpty()
                    ? NameFormatter.capitalizeName(studentDatabase.getName())
                    : NameFormatter.capitalizeName(studentUpdate.getName()));

            ObjectMapper mapper = new ObjectMapper();

            log.info("Updating student: " + mapper.writeValueAsString(studentDatabase));

            studentRepository.save(studentDatabase);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Updated the student successfully\"}";

            log.info(body);

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ErrorResponse.build(e);
        }

    }

    @Transactional
    public ResponseEntity<String> deleteStudentByStudentRegistrationNumber(Integer studentRegistrationNumber) {
        try {

            Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            ObjectMapper mapper = new ObjectMapper();

            log.info("Deleting student: " + mapper.writeValueAsString(student));

            studentRepository.deleteById(student.getStudentRegistrationNumber());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Deleted the student successfully\"}";

            log.info(body);

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ErrorResponse.build(e);
        }

    }

    @Transactional(readOnly = true)
    public List<Student> findByName(String name) {
        log.info("Searching for student name: " + name);
        return studentRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Student findByStudentRegistrationNumber(Integer studentRegistrationNumber) {
        log.info("Searching for student registration number: " + studentRegistrationNumber);
        return studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);
    }

}

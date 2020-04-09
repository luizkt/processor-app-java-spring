package br.com.fiap.service;

import java.util.List;

import br.com.fiap.entity.Student;
import br.com.fiap.repository.StudentRepository;
import br.com.fiap.utils.NameFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private static final Logger logger = LogManager.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public ResponseEntity<String> add(Student student) {

        try {
            student.setName(NameFormatter.capitalizeName(student.getName()));

            if (studentRepository.existsById(student.getStudentRegistrationNumber()))
                throw new Exception("\"Student registration number already exist\"");

            studentRepository.save(student);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Added the student successfully\"}";

            logger.info(body);

            return new ResponseEntity<>(body, headers, HttpStatus.CREATED);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"An error has occurred\", \"exception\":" + e.getMessage() + "}";

            logger.error(e);

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
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

            studentRepository.save(studentDatabase);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Updated the student successfully\"}";

            logger.info(body);

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"An error has occurred\", \"exception\":" + e.getMessage() + "}";

            logger.error(e);

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
    public ResponseEntity<String> deleteStudentByStudentRegistrationNumber(Integer studentRegistrationNumber) {
        try {

            Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);

            studentRepository.deleteById(student.getStudentRegistrationNumber());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"Deleted the student successfully\"}";

            logger.info(body);

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
            String body = "{\"message\":\"An error has occurred\", \"exception\":" + e.getMessage() + "}";

            logger.error(e);

            return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional(readOnly = true)
    public List<Student> findByName(String name) {
        logger.info("Searching for student name");
        return studentRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Student findByStudentRegistrationNumber(Integer studentRegistrationNumber) {
        logger.info("Searching for student registration number");
        return studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);
    }

}

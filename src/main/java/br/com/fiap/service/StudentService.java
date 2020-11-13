package br.com.fiap.service;

import br.com.fiap.entity.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface StudentService {
    ResponseEntity<String> add(Student student) throws JsonProcessingException;
    ResponseEntity<String> updateStudentByStudentRegistrationNumber(Student studentUpdate, Integer studentRegistrationNumber) throws JsonProcessingException;
    ResponseEntity<String> deleteStudentByStudentRegistrationNumber(Integer studentRegistrationNumber) throws JsonProcessingException;
    ResponseEntity<String> findByName(String name) throws JsonProcessingException;
    ResponseEntity<String> findByStudentRegistrationNumber(Integer studentRegistrationNumber) throws JsonProcessingException;
}

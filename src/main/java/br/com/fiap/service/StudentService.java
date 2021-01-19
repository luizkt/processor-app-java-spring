package br.com.fiap.service;

import br.com.fiap.entity.ApplicationResponseBody;
import br.com.fiap.entity.Student;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface StudentService {
    ApplicationResponseBody add(Student student) throws JsonProcessingException;
    ApplicationResponseBody updateStudentByStudentRegistrationNumber(Student studentUpdate, Integer studentRegistrationNumber) throws JsonProcessingException;
    ApplicationResponseBody deleteStudentByStudentRegistrationNumber(Integer studentRegistrationNumber) throws JsonProcessingException;
    ApplicationResponseBody findByName(String name) throws JsonProcessingException;
    ApplicationResponseBody findByStudentRegistrationNumber(Integer studentRegistrationNumber) throws JsonProcessingException;
}

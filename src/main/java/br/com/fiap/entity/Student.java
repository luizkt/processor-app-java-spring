package br.com.fiap.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="STUDENT", uniqueConstraints = {@UniqueConstraint(columnNames = "STUDENT_REGISTRATION_NUMBER")})
public class Student implements Serializable{

    @Id
    @Column(name = "STUDENT_REGISTRATION_NUMBER", unique = true, nullable = false)
    private Integer studentRegistrationNumber;

    @Column(name = "NAME")
    private String name;

    public Integer getStudentRegistrationNumber() {
        return studentRegistrationNumber;
    }

    public void setStudentRegistrationNumber(Integer studentRegistrationNumber) {
        this.studentRegistrationNumber = studentRegistrationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

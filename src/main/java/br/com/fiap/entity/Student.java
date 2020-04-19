package br.com.fiap.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "STUDENT", uniqueConstraints = {@UniqueConstraint(columnNames = "STUDENT_REGISTRATION_NUMBER")})
public class Student implements Serializable {

    public Student(){}

    public Student(Integer studentRegistrationNumber, String name){
        this.studentRegistrationNumber = studentRegistrationNumber;
        this.name = name;
    }

    @Id
    @Column(name = "STUDENT_REGISTRATION_NUMBER", unique = true, nullable = false)
    @JsonProperty("student_registration_number")
    @NotNull
    @ApiModelProperty(value = "Institution's registration number")
    private Integer studentRegistrationNumber;

    @Column(name = "NAME")
    @JsonProperty("name")
    @NotNull
    @ApiModelProperty(value = "Student's name")
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

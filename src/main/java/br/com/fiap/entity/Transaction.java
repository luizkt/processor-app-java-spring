package br.com.fiap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "TRANSACTION", uniqueConstraints = {@UniqueConstraint(columnNames = "TRANSACTION_ID")})
public class Transaction implements Serializable {

    @Id
    @Column(name = "TRANSACTION_ID", unique = true, nullable = false)
    @JsonProperty("transaction_id")
    @NotNull
    private Integer transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_REGISTRATION_NUMBER")
    @JsonIgnore
    private Student student;

    @JsonProperty("student_registration_number")
    @Transient
    private Integer studentRegistrationNumber;

    @Column(name = "PAN_FINAL")
    @JsonProperty("pan_final")
    @NotNull
    private String panFinal;

    @Column(name = "AMOUNT")
    @JsonProperty("amount")
    @NotNull
    private Double amount;

    @Column(name = "DESCRIPTION")
    @JsonProperty("description")
    private String description;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getStudentRegistrationNumber() {
        return studentRegistrationNumber;
    }

    public void setStudentRegistrationNumber(Integer studentRegistrationNumber) {
        this.studentRegistrationNumber = studentRegistrationNumber;
    }

    public String getPanFinal() {
        return panFinal;
    }

    public void setPanFinal(String panFinal) {
        this.panFinal = panFinal;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

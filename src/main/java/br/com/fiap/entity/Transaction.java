package br.com.fiap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "TRANSACTION", uniqueConstraints = {@UniqueConstraint(columnNames = "TRANSACTION_ID")})
public class Transaction implements Serializable {

    public Transaction() {
    }

    public Transaction(Integer transactionId, Integer studentRegistrationNumber, String panFinal, Double amount, String description) {
        this.transactionId = transactionId;
        this.studentRegistrationNumber = studentRegistrationNumber;
        this.panFinal = panFinal;
        this.amount = amount;
        this.description = description;
    }

    public Transaction(Integer transactionId, Student student, Integer studentRegistrationNumber, String panFinal, Double amount, String description) {
        this.transactionId = transactionId;
        this.student = student;
        this.studentRegistrationNumber = studentRegistrationNumber;
        this.panFinal = panFinal;
        this.amount = amount;
        this.description = description;
    }

    @Id
    @Column(name = "TRANSACTION_ID", unique = true, nullable = false)
    @JsonProperty("transaction_id")
    @NotNull
    @ApiModelProperty(value = "Transaction identification")
    private Integer transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_REGISTRATION_NUMBER")
    @JsonIgnore
    private Student student;

    @JsonProperty("student_registration_number")
    @Transient
    @ApiModelProperty(value = "Institution's registration number")
    private Integer studentRegistrationNumber;

    @Column(name = "PAN_FINAL")
    @JsonProperty("pan_final")
    @NotNull
    @ApiModelProperty(value = "Last 4 digits from students credit card")
    private String panFinal;

    @Column(name = "AMOUNT")
    @JsonProperty("amount")
    @NotNull
    @ApiModelProperty(value = "Amount of the transaction")
    private Double amount;

    @Column(name = "DESCRIPTION")
    @JsonProperty("description")
    @ApiModelProperty(value = "Description of the transaction")
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

    public String toJsonString() {
        return "{" +
                "\"transaction_id\":" + transactionId + "," +
                "\"student_registration_number\":" + studentRegistrationNumber + "," +
                "\"pan_final\":\"" + panFinal + "\"," +
                "\"amount\":" + amount + "," +
                "\"description\":\"" + description + "\"" +
                "}";
    }
}

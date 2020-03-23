package br.com.fiap.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class TransactionJson {
    @JsonProperty("transaction_id")
    @NotNull
    private Integer transactionId;

    @JsonProperty("student_registration_number")
    @NotNull
    private Integer studentRegistrationNumber;

    @JsonProperty("pan_final")
    @NotNull
    private String panFinal;

    @JsonProperty("amount")
    @NotNull
    private Double amount;

    @JsonProperty("description")
    private String description;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
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

package br.com.fiap.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TRANSACTION", uniqueConstraints = {@UniqueConstraint(columnNames = "TRANSACTION_ID")})
public class Transaction implements Serializable {

    @Id
    @Column(name = "TRANSACTION_ID", unique = true, nullable = false)
    private Integer transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_REGISTRATION_NUMBER")
    private Student student;

    @Column(name = "PAN_FINAL")
    private String panFinal;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "DESCRIPTION")
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

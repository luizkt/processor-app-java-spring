package br.com.fiap.repository;

import br.com.fiap.entity.Student;
import br.com.fiap.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.student = :studentRegistrationNumber")
    public List<Transaction> findAllTransactionsFromStudent(@Param("studentRegistrationNumber") Student studentRegistrationNumber);
}

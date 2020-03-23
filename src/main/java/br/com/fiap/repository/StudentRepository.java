package br.com.fiap.repository;

import java.util.List;

import br.com.fiap.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

	@Query("select s from Student s where s.name = :name")
	public List<Student> findByName(@Param("name") String name);
	
	@Query("select s from Student s where s.studentRegistrationNumber = :studentRegistrationNumber")
	public List<Student> findByStudentRegistrationNumber(@Param("studentRegistrationNumber") Integer studentRegistrationNumber);
	
}

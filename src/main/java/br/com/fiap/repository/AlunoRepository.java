package br.com.fiap.repository;

import java.util.List;

import br.com.fiap.entity.Aluno;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Integer> {

	@Query("select a from Aluno a where a.nome = :nome")
	public List<Aluno> findByName(@Param("nome") String nome);
	
	@Query("select a from Aluno a where a.matricula = :matricula")
	public List<Aluno> findByMatricula(@Param("matricula") String cpf);
	
}

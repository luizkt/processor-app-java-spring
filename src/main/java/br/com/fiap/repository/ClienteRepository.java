package br.com.fiap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fiap.entity.Cliente;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Integer> {

	@Query("select c from Cliente c where c.nome = :nome")
	public List<Cliente> findByName(@Param("nome") String nome);
	
	@Query("select c from Cliente c where c.cpf = :cpf")
	public List<Cliente> findByDocument(@Param("cpf") String cpf);
	
	@Query("select c from Cliente c where c.uuid = :uuid")
	public List<Cliente> findByUuid(@Param("uuid") String uuid);
	
//	@Query("select e from Endereco e where e.rua = :rua")
//	public List<Endereco> findByStreet(@Param("rua") String rua);
//	
//	@Query("select e from Endereco e where e.cep = :cep")
//	public List<Endereco> findByPostalCode(@Param("cep") String cep);
//
//	@Query("select e from Endereco e where e.bairro = :bairro")
//	public List<Endereco> findByDistrict(@Param("bairro") String bairro);
//	
//	@Query("select e from Endereco e where e.cidade = :cidade")
//	public List<Endereco> findByCity(@Param("cidade") String cidade);
//	
//	@Query("select e from Endereco e where e.estado = :estado")
//	public List<Endereco> findByState(@Param("estado") String estado);
//	
//	@Query("select e from Endereco e where e.pais = :pais")
//	public List<Endereco> findByCountry(@Param("pais") String pais);
	
}

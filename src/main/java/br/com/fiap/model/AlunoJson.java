package br.com.fiap.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlunoJson {

	@JsonProperty("matricula")
	@NotNull
	private String matricula;
	
	@JsonProperty("nome")
	@NotNull
	private String nome;

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}

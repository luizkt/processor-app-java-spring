package br.com.fiap.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClienteJson {

	@JsonProperty("cpf")
	@NotNull
	private String cpf;
	
	@JsonProperty("uuid")
	@NotNull
	private String uuid;

	@JsonProperty("nome")
	@NotNull
	private String nome;

	@JsonProperty("email")
	@NotNull
	private String email;

	@JsonProperty("data_nascimento")
	private String dataNascimento;

	@JsonProperty("endereco")
	private EnderecoJson endereco;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public EnderecoJson getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoJson endereco) {
		this.endereco = endereco;
	}

}

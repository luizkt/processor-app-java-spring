package br.com.fiap.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ALUNO", uniqueConstraints = {@UniqueConstraint(columnNames = "ALUNO_ID")})
public class Aluno implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ALUNO_ID", unique = true, nullable = false)
    private Integer alunoId;

    @Column(name = "MATRICULA", unique = true, nullable = false)
    private String matricula;

    @Column(name = "NOME")
    private String nome;

    public Integer getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Integer alunoId) {
        this.alunoId = alunoId;
    }

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

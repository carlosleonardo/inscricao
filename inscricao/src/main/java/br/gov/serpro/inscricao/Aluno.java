package br.gov.serpro.inscricao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Aluno {
	public Aluno() 
	{
	}

	@Override
	public boolean equals(Object outro) {
		return ((Aluno)outro).nome.equals(this.nome);
	}

	public Aluno(String nome) {
		this.nome = nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
	
	public void setMatricula(Integer matricula) {
		this.matricula = matricula;
	}

	public Integer getMatricula() {
		return matricula;
	}

	private String nome;
	
	@Id
	@GeneratedValue
	private Integer matricula;
}

package com.example.demo_proc.models;

import java.util.ArrayList;
import java.util.List;

public class NohOuRamo {

	private String classeNome;
	private String nomePropNoh;
	private String valorPropRamo;
	private String valorClasseFolha;
	private List<NohOuRamo> filhos;
	private Integer id;

	public NohOuRamo() {
		this.filhos = new ArrayList<NohOuRamo>();
	}

	public NohOuRamo(String classeNome, String nomePropNoh, String valorPropRamo, String valorClasseFolha, List<NohOuRamo> filho) {
		this.nomePropNoh = nomePropNoh;
		this.valorPropRamo = valorPropRamo;
		this.valorClasseFolha = valorClasseFolha;
		this.filhos = filho;
		this.classeNome = classeNome;
	}

	public String getClasseNome() {
		return classeNome;
	}

	public void setClasseNome(String classeNome) {
		this.classeNome = classeNome;
	}

	public String getNomePropNoh() {
		return nomePropNoh;
	}

	public void setNomePropNoh(String nomePropNoh) {
		this.nomePropNoh = nomePropNoh;
	}

	public String getValorPropRamo() {
		return valorPropRamo;
	}

	public void setValorPropRamo(String valorPropRamo) {
		this.valorPropRamo = valorPropRamo;
	}

	public String getValorClasseFolha() {
		return valorClasseFolha;
	}

	public void setValorClasseFolha(String valorClasseFolha) {
		this.valorClasseFolha = valorClasseFolha;
	}

	public List<NohOuRamo> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<NohOuRamo> filhos) {
		this.filhos = filhos;
	}
	
	public void addFilhos(NohOuRamo filho) {
		this.getFilhos().add(filho);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

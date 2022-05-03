package com.example.demo_proc.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class ModelResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Integer> idCaminho;
	private NohOuRamo nohFinal;
	private String riscoFinal;
	
	public ModelResponse() {
		setIdCaminho(new ArrayList<Integer>());
	}

	public List<Integer> getIdCaminho() {
		return idCaminho;
	}

	public void setIdCaminho(List<Integer> idCaminho) {
		this.idCaminho = idCaminho;
	}

	public NohOuRamo getNohFinal() {
		return nohFinal;
	}

	public void setNohFinal(NohOuRamo nohFinal) {
		this.nohFinal = nohFinal;
	}

	public String getRiscoFinal() {
		return riscoFinal;
	}

	public void setRiscoFinal(String riscoFinal) {
		this.riscoFinal = riscoFinal;
	}
}

package com.example.demo_proc.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class ModelRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank
	@NotNull
	private String divida;
	
	@NotBlank
	@NotNull
	private String garantia;
	
	@NotBlank
	@NotNull
	private String historia;
	
	@NotBlank
	@NotNull
	private String renda;

	public String getDivida() {
		return divida;
	}

	public void setDivida(String divida) {
		this.divida = divida;
	}

	public String getGarantia() {
		return garantia;
	}

	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}

	public String getHistoria() {
		return historia;
	}

	public void setHistoria(String historia) {
		this.historia = historia;
	}

	public String getRenda() {
		return renda;
	}

	public void setRenda(String renda) {
		this.renda = renda;
	}
}

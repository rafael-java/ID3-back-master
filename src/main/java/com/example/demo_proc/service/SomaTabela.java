package com.example.demo_proc.service;

import java.util.Map;

import com.example.demo_proc.models.ListasPadrao;

public class SomaTabela {
	private static SomaTabela instanciaUnica;
	private static Double entropiaTabela;
	private static Double tamanhoTabela;

	static {
		instanciaUnica = new SomaTabela();
	}
	
	private SomaTabela() {
		ListasPadrao l = new ListasPadrao();
		l.setClassePadrao();
		l.setListaDadosPadrao();
		l.setPropsPadrao();
		Entropia e = new Entropia(l.getProps(), l.getListaDados());
		
		Map<String, Double> dadosCE = e.dadosCE();
		setEntropiaTabela(dadosCE.get("Entropia da Tabela"));
		setTamanhoTabela(dadosCE.get("Tamanho da Tabela"));
	}

	public static synchronized SomaTabela getInstance() {
		return instanciaUnica;
	}


	public static Double getTamanhoTabela() {
		return tamanhoTabela;
	}

	public static void setTamanhoTabela(Double tamanhoTabela) {
		SomaTabela.tamanhoTabela = tamanhoTabela;
	}

	public static Double getEntropiaTabela() {
		return entropiaTabela;
	}

	public static void setEntropiaTabela(Double entropiaTabela) {
		SomaTabela.entropiaTabela = entropiaTabela;
	}
}

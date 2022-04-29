package com.example.demo_proc.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListasPadrao {

	private List<Map<Integer, String>> listaDados;
	private Map<String, Integer> props;
	private String classe;
	
	public void setClassePadrao() {
		this.classe = "risco";
	}
	
	public String getClasse() {
		return classe;
	}
	
	public String getClasseFormatado() {
		return classe.substring(0, 1).toUpperCase() + classe.substring(1);
	}

	public void setListaDadosPadrao() {
		listaDados = new ArrayList<Map<Integer, String>>();
		
		Map<Integer, String> risco = new HashMap<Integer, String>();
		Map<Integer, String> historico = new HashMap<Integer, String>();
		Map<Integer, String> garantia = new HashMap<Integer, String>();
		Map<Integer, String> divida = new HashMap<Integer, String>();
		Map<Integer, String> renda = new HashMap<Integer, String>();
		
		risco.put(1, "alto");
		risco.put(2, "alto");
		risco.put(3, "moderado");
		risco.put(4, "alto");
		risco.put(5, "baixo");
		risco.put(6, "baixo");
		risco.put(7, "alto");
		risco.put(8, "moderado");
		risco.put(9, "baixo");
		risco.put(10, "baixo");
		risco.put(11, "alto");
		risco.put(12, "moderado");
		risco.put(13, "baixo");
		risco.put(14, "alto");

		historico.put(1,"ruim");
		historico.put(2,"desconhecida");
		historico.put(3,"desconhecida");
		historico.put(4, "desconhecida");
		historico.put(5, "desconhecida");
		historico.put(6, "desconhecida");
		historico.put(7, "ruim");
		historico.put(8, "ruim");
		historico.put(9, "boa");
		historico.put(10, "boa");
		historico.put(11, "boa");
		historico.put(12, "boa");
		historico.put(13, "boa");
		historico.put(14, "ruim");
		
		garantia.put(1,"nenhuma");
		garantia.put(2,"nenhuma");
		garantia.put(3,"nenhuma");
		garantia.put(4, "nenhuma");
		garantia.put(5, "nenhuma");
		garantia.put(6, "adequada");
		garantia.put(7, "nenhuma");
		garantia.put(8, "adequada");
		garantia.put(9, "nenhuma");
		garantia.put(10, "adequada");
		garantia.put(11, "nenhuma");
		garantia.put(12, "nenhuma");
		garantia.put(13, "nenhuma");
		garantia.put(14, "nenhuma");
		
		divida.put(1,"alta");
		divida.put(2,"alta");
		divida.put(3,"baixa");
		divida.put(4, "baixa");
		divida.put(5, "baixa");
		divida.put(6, "baixa");
		divida.put(7, "baixa");
		divida.put(8, "baixa");
		divida.put(9, "baixa");
		divida.put(10, "alta");
		divida.put(11, "alta");
		divida.put(12, "alta");
		divida.put(13, "alta");
		divida.put(14, "alta");
		
		renda.put(1,"0 a 15");
		renda.put(2,"15 a 35");
		renda.put(3,"15 a 35");
		renda.put(4, "0 a 15");
		renda.put(5, "> 35");
		renda.put(6, "> 35");
		renda.put(7, "0 a 15");
		renda.put(8, "> 35");
		renda.put(9, "> 35");
		renda.put(10, "> 35");
		renda.put(11, "0 a 15");
		renda.put(12, "15 a 35");
		renda.put(13, "> 35");
		renda.put(14, "15 a 35");
		
		listaDados.add(risco);
		listaDados.add(historico);
		listaDados.add(garantia);
		listaDados.add(divida);
		listaDados.add(renda);
	}

	public void setPropsPadrao() {
		props = new HashMap<String, Integer>();

		props.put("História de Crédito", 1);
		props.put("Garantia", 2);
		props.put("Dívida", 3);
		props.put("Renda", 4);
	}
	
	public void setListaDados(List<Map<Integer, String>> listaDados) {
		this.listaDados = listaDados;
	}

	public void setProps(Map<String, Integer> props) {
		this.props = props;
	}
	
	public Map<String, Integer> getProps() {
		return props;
	}
	
	public List<Map<Integer, String>> getListaDados() {
		return listaDados;
	}

	public void setListaDadosTeste() {
		listaDados = new ArrayList<Map<Integer, String>>();
		
		Map<Integer, String> risco = new HashMap<Integer, String>();
		Map<Integer, String> historico = new HashMap<Integer, String>();
		Map<Integer, String> garantia = new HashMap<Integer, String>();
		Map<Integer, String> divida = new HashMap<Integer, String>();
		Map<Integer, String> renda = new HashMap<Integer, String>();
		
		risco.put(1, "alto");
		risco.put(2, "alto");
		risco.put(3, "moderado");
//		risco.put(4, "alto");

		historico.put(1,"ruim");
		historico.put(2,"desconhecida");
		historico.put(3,"desconhecida");
//		historico.put(4, "desconhecida");
		
		garantia.put(1,"nenhuma");
		garantia.put(2,"nenhuma");
		garantia.put(3,"nenhuma");
//		garantia.put(4, "nenhuma");
		
		divida.put(1,"alta");
		divida.put(2,"alta");
		divida.put(3,"baixa");
//		divida.put(4, "baixa");
		
		renda.put(1,"0 a 15");
		renda.put(2,"15 a 35");
		renda.put(3,"15 a 35");
//		renda.put(4, "0 a 15");
		
		listaDados.add(risco);
		listaDados.add(historico);
		listaDados.add(garantia);
		listaDados.add(divida);
		listaDados.add(renda);
	}

	
	
}

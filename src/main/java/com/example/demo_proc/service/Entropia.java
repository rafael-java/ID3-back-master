package com.example.demo_proc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.lang.Math.*;

@SuppressWarnings("unused")
public class Entropia {

	private List<Map<Integer, String>> listaDados;
	private Map<Integer, String> propsInvertida;
	private Double entropiaTabela;
	private Double tamanhoTabela;

	public Entropia(Map<String, Integer> propsAntiga, List<Map<Integer, String>> listaDados) {
		this.listaDados = listaDados;
		this.propsInvertida = inverterProps(propsAntiga);
		this.entropiaTabela = SomaTabela.getEntropiaTabela();
		this.tamanhoTabela = SomaTabela.getTamanhoTabela();
	}

	public Map<String, Double> dadosCE() {
		Map<String, Double> dadosCE = new HashMap<String, Double>();

		Map<String, Double> countValores = countValores();
		dadosCE.put("Tamanho da Tabela", countValores.get("Tamanho da Tabela"));

		Map<String, Double> PValores = P_TabelaCE(countValores);
		dadosCE.put("Entropia da Tabela", entropiaEsoma(PValores));

		return dadosCE;
	}

	public void main() {
		processaTudo();
	}

	public void processaTudo() {
		Map<String, Double> paraDecidir = new HashMap<String, Double>();

		for (Integer indice : propsInvertida.keySet()) {
			String p = propsInvertida.get(indice);

			Map<Integer, String> coluna = listaDados.get(indice);

			Map<String, Double> dadosCE = new HashMap<String, Double>();

			Map<String, Double> totalValoresProps = totalValoresProps(coluna);

			List<Map<String, String>> subConjunto = subConjunto(totalValoresProps, indice);
			List<List<Map<String, Double>>> countProps = countProps(subConjunto);

			if (checkSeTudoOk(countProps)) {

				adicionaFrequenciaAPartirDoTotal(countProps);

				List<Map<String, Double>> pValoresProp = pValoresProp(countProps);

				Map<String, Double> entropiaValoresProps = entropiaValoresProps(pValoresProp);

				List<Double> valoresPropsPonderado = valoresPropsPonderado(entropiaValoresProps);
			}

			// List com -> ponderado (do Total * soma + ... + ...)
			// Ganho de informacao (SomaTabela.getEntropiaDaTabela() - Ponderado)
		}
	}

	private List<Double> valoresPropsPonderado(Map<String, Double> entropiaValoresProps) {
		Double doTotal = 0D;
		List<Double> ponderados = new ArrayList<Double>();
		for (Double valor : entropiaValoresProps.values()) {
			ponderados.add(valor);
		}
		return null;
	}

	private Map<String, Double> entropiaValoresProps(List<Map<String, Double>> pProps) {

		Map<String, Double> entropias = new HashMap<String, Double>();

		for (Map<String, Double> map : pProps) {

			Set<String> keySet = map.keySet();
			String prop = "";

			for (String k : keySet) {
				if (!k.equals("total") && !k.equals("freqDoTotal")) {
					String[] propArr = k.split("\\|");
					prop = propArr[0];
					Double soma = entropiaEsoma(map);
					entropias.put(prop, soma);
				}
			}
		}

		return entropias;
	}

	private List<Map<String, Double>> pValoresProp(List<List<Map<String, Double>>> countProps)
			throws IllegalArgumentException {

		List<Map<String, Double>> gabarito = countProps.get(1);
		List<Map<String, Double>> todasFreqs = new ArrayList<Map<String, Double>>();
		Integer index = 0;

		for (Map<String, Double> map : countProps.get(0)) {
			Set<String> propSet = gabarito.get(index).keySet();
			String prop = "";
			if (propSet.size() > 1) {
				throw new IllegalArgumentException("Gabarito tem algum map com mais de uma entrada");
			}
			for (String string : propSet) {
				prop = string;
			}

			index++;

			Map<String, Double> freqs = new HashMap<String, Double>();
			Double totalProp = map.get("total");
			Double freq = 0D;
			for (String string : map.keySet()) {
				if (!string.equals("total") && !string.equals("freqDoTotal")) {
					freq = map.get(string) / totalProp;
					freqs.put(prop + "|" + string, freq);
				}
			}

			freqs.put("total", map.get("total"));
			freqs.put("freqDoTotal", map.get("freqDoTotal"));

			todasFreqs.add(freqs);
		}

		return todasFreqs;
	}

	private void adicionaFrequenciaAPartirDoTotal(List<List<Map<String, Double>>> countProps) {
		List<Map<String, Double>> todasFreq = countProps.get(0);

		for (Map<String, Double> map : todasFreq) {

			Double total = 0D;
			Double freq = 0D;

			if (map.containsKey("total")) {
				total = map.get("total");
				freq = total / SomaTabela.getTamanhoTabela();
			}

			map.put("freqDoTotal", freq);
		}
	}

	public Boolean checkSeTudoOk(List<List<Map<String, Double>>> countProps) throws UnsupportedOperationException {

		List<Map<String, Double>> todasFreq = countProps.get(0);
		List<Map<String, Double>> gabarito = countProps.get(1);
		Double total = 0D;

		for (Map<String, Double> map : todasFreq) {
			if (map.containsKey("total")) {
				total = total + map.get("total");
			} else {
				throw new UnsupportedOperationException("Um dos maps não tem total");
			}
		}

		if (total.doubleValue() == SomaTabela.getTamanhoTabela()) {
			return true;
		} else {
			throw new UnsupportedOperationException("Total não é igual ao tabela");
		}
	}

	public List<Map<String, String>> subConjunto(Map<String, Double> countValores, Integer indice) {
		List<Map<String, String>> subConjunto = new ArrayList<Map<String, String>>();
		for (String valor : countValores.keySet()) {
			Integer count = 0;

			for (String classeValor : listaDados.get(indice).values()) {
				Map<String, String> sc = new HashMap<String, String>();
				count++;
				if (classeValor.equals(valor)) {
					sc.put(valor, listaDados.get(0).get(count));
					subConjunto.add(sc);
				}
			}
		}
		return subConjunto;
	}

	public List<List<Map<String, Double>>> countProps(List<Map<String, String>> subConjunto) {
		List<Map<String, Double>> todasFreq = new ArrayList<Map<String, Double>>();
		List<Map<String, Double>> gabarito = new ArrayList<Map<String, Double>>();
		List<List<Map<String, Double>>> retorno = new ArrayList<List<Map<String, Double>>>();

		String stringAnterior = "";

		Integer count = 0;
		Map<String, Double> pValoresProp = new HashMap<String, Double>();
		Integer total = 0;
		pValoresProp.put("total", total.doubleValue());

		Integer indc = 0;
		Map<String, Double> nomeProp = new HashMap<String, Double>();

		for (Map<String, String> map : subConjunto) {
			Double p = 1D;

			for (String string : map.keySet()) {
				if (!stringAnterior.equals(string)) {
					count++;
					if (count > 1) {
						pValoresProp = new HashMap<String, Double>();
						total = 0;
						pValoresProp.put("total", total.doubleValue());
						nomeProp = new HashMap<String, Double>();
						indc++;
					}
					nomeProp.put(string, indc.doubleValue());

					todasFreq.add(pValoresProp);
					gabarito.add(nomeProp);
				}
				stringAnterior = string;

				if (!pValoresProp.keySet().contains(map.get(string))) {
					pValoresProp.put(map.get(string), p);
				} else {
					pValoresProp.put(map.get(string), pValoresProp.get(map.get(string)) + 1);
				}

				total++;
				pValoresProp.put("total", total.doubleValue());
			}
		}

		retorno.add(todasFreq);
		retorno.add(gabarito);
		return retorno;
	}

	public Map<Integer, String> inverterProps(Map<String, Integer> propsAntiga) {

		Map<String, Integer> propsNovo = new HashMap<String, Integer>();

		propsNovo.putAll(propsAntiga);

		Map<Integer, String> propsInvertida = new HashMap<Integer, String>();

		for (Iterator<String> iterator = propsNovo.keySet().iterator(); iterator.hasNext();) {
			String prop = iterator.next();
			propsInvertida.put(propsNovo.get(prop), prop);
		}

		return propsInvertida;
	}

	public static Double log2(Double f) {
		Double log2 = 0D;
		if (f != 0) {
			log2 = log(f) / log(2.0);
		}
		return log2;
	}

	private Double entropiaEsoma(Map<String, Double> PValores) {
		Double soma = 0D;

		Map<String, Double> soPraEuSaber = new HashMap<String, Double>();
		Set<String> valores = PValores.keySet();
		for (String valor : valores) {
			if (!valor.equals("total") && !valor.equals("freqDoTotal")) {
				Double P = PValores.get(valor);
				Double entropia = -1 * P * log2(P);
				soPraEuSaber.put(valor, entropia);
				soma = soma + entropia;
			}
		}

		return soma;
	}

	private Map<String, Double> P_TabelaCE(Map<String, Double> countValores) {

		Map<String, Double> PValores = new HashMap<String, Double>();

		Double tamanhoTabela = countValores.get("Tamanho da Tabela");
		Set<String> valores = countValores.keySet();
		valores.remove("Tamanho da Tabela");
		for (String valor : valores) {
			Double count = countValores.get(valor);
			PValores.put(valor, count / tamanhoTabela);
		}

		return PValores;
	}

	private Map<String, Double> totalValoresProps(Map<Integer, String> coluna) {
		Collection<String> valores = coluna.values();
		List<String> valoresUnicos = new ArrayList<String>();
		for (String string : valores) {
			if (!valoresUnicos.contains(string)) {
				valoresUnicos.add(string);
			}
		}

		Map<String, Double> countValores = new HashMap<String, Double>();
		for (String valorUnico : valoresUnicos) {
			countValores.put("Tamanho da Tabela", Double.valueOf(valores.size()));
			countValores.put(valorUnico, 0D);
		}

		for (String valor : valores) {
			for (String valorUnico : valoresUnicos) {
				if (valor.equals(valorUnico)) {
					countValores.put(valorUnico, countValores.get(valorUnico) + 1);
				}
			}
		}

		return countValores;
	}

	private Map<String, Double> countValores() {
		Map<Integer, String> classe = listaDados.get(0);
		Collection<String> valores = classe.values();
		List<String> valoresUnicos = new ArrayList<String>();
		for (String string : valores) {
			if (!valoresUnicos.contains(string)) {
				valoresUnicos.add(string);
			}
		}

		Map<String, Double> countValores = new HashMap<String, Double>();
		for (String valorUnico : valoresUnicos) {
			countValores.put("Tamanho da Tabela", Double.valueOf(valores.size()));
			countValores.put(valorUnico, 0D);
		}

		for (String valor : valores) {
			for (String valorUnico : valoresUnicos) {
				if (valor.equals(valorUnico)) {
					countValores.put(valorUnico, countValores.get(valorUnico) + 1);
				}
			}
		}

		return countValores;
	}

//	public String selecionaPropriedade() {
//		String value = new String();
//		for (String key : props.keySet()) {
//			value = key;
//			break;
//		}
//		return value;
//	}

	public void print() {

		System.out.println(listaDados.get(0));

//		for (Map<Integer, String> teste : listaDados) {
//			Collection<String> a = teste.values();
//			System.out.println(a.toString());
//			System.out.println("Tudo:" + teste.toString());
//			System.out.println("Todas as keys:" + propsInvertida.keySet());
//			System.out.println("todos os valores:" + propsInvertida.values());
//			System.out.println("Tamanho: " + teste.size());
//			System.out.println("Valor do Nome 1: " + teste.get("Nome 1"));		
//			}		
	}

}

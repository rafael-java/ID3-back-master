package com.example.demo_proc.service;

import static java.lang.Math.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Selecao {

	private List<Map<Integer, String>> listaDados;
	private Map<Integer, String> propsInvertida;
	private Map<String, Double> dadosCE;

	private final static String ENTROPIA_DA_TABELA = "Entropia da Tabela";
	private final static String TAMANHO_DA_TABELA = "Tamanho da Tabela";
	private final static String TOTAL = "Total";
	private final static String FREQ_DO_TOTAL = "FreqDoTotal";

	public Selecao(Map<String, Integer> propsNaoInvertida, List<Map<Integer, String>> listaDados) {
		this.listaDados = listaDados;
		this.propsInvertida = inverterProps(propsNaoInvertida);
		this.dadosCE = dadosCE();
	}

	private Map<Integer, String> inverterProps(Map<String, Integer> propsAntiga) {

		Map<String, Integer> propsNovo = new HashMap<String, Integer>();

		propsNovo.putAll(propsAntiga);

		Map<Integer, String> propsInvertida = new HashMap<Integer, String>();

		for (Iterator<String> iterator = propsNovo.keySet().iterator(); iterator.hasNext();) {
			String prop = iterator.next();
			propsInvertida.put(propsNovo.get(prop), prop);
		}

		return propsInvertida;
	}

	private Double entropiaEsoma(Map<String, Double> PValores) {
		Double soma = 0D;

		Map<String, Double> soPraEuSaber = new HashMap<String, Double>();
		Set<String> valores = PValores.keySet();
		for (String valor : valores) {
			if (!valor.equals(TOTAL) && !valor.equals(FREQ_DO_TOTAL)) {
				Double P = PValores.get(valor);
				Double entropia = -1 * P * log2(P);
				soPraEuSaber.put(valor, entropia);
				soma = soma + entropia;
			}
		}

		return soma;
	}

	private static Double log2(Double f) {
		Double log2 = 0D;
		if (f != 0) {
			log2 = log(f) / log(2.0);
		}
		return log2;
	}

	// - - -

	public String elegeOMaior() {

		Map<String, Double> ganhosDeInformacao = processaTudo();
		Map<String, Double> maior = new HashMap<String, Double>();

		Double maiorGanho = 0.00;
		String prop = "";

		for (String string : ganhosDeInformacao.keySet()) {
			if (ganhosDeInformacao.get(string) > maiorGanho) {
				maiorGanho = ganhosDeInformacao.get(string);
				prop = string;
			}
		}

//		System.out.println(ganhosDeInformacao);
		maior.put(prop, maiorGanho);
//		System.out.println(maior);

		return prop;
	}

	private Map<String, Double> processaTudo() {
		System.out.println("-");
		Map<String, Double> ganhosDeInformacao = new HashMap<String, Double>();

		for (Integer indice : propsInvertida.keySet()) {
			String prop = propsInvertida.get(indice);

			Map<Integer, String> coluna = listaDados.get(indice);

			Map<String, Double> totalValoresProps = totalValoresProps(coluna);

			List<Map<String, String>> contagemDetalhada = contagemDetalhada(totalValoresProps, coluna, indice);
			List<List<Map<String, Double>>> countProps = countProps(contagemDetalhada);

			if (checkSeTudoOk(countProps)) {

				adicionaFrequenciaAPartirDoTotal(countProps);

				List<Map<String, Double>> pValoresProp = pValoresProp(countProps);

				Map<String, Double> entropiaValoresProps = entropiaESomaValoresProps(pValoresProp);

				List<Double> valoresPropsPonderado = valoresPropsPonderado(entropiaValoresProps);

				Double somaPonderado = somaPonderado(valoresPropsPonderado);

				Double ganhoDeInformacao = ganhoDeInformacao(somaPonderado);

				ganhosDeInformacao.put(prop, ganhoDeInformacao);
			}
		}
		return ganhosDeInformacao;
	}

	// - - -

	private Map<String, Double> dadosCE() {
		Map<String, Double> dadosCE = new HashMap<String, Double>();

		Map<String, Double> countValores = countValores();
		dadosCE.put(TAMANHO_DA_TABELA, countValores.get(TAMANHO_DA_TABELA));

		Map<String, Double> PValores = P_TabelaCE(countValores);
		dadosCE.put(ENTROPIA_DA_TABELA, entropiaEsoma(PValores));

		return dadosCE;
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
			countValores.put(TAMANHO_DA_TABELA, Double.valueOf(valores.size()));
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

	private Map<String, Double> P_TabelaCE(Map<String, Double> countValores) {

		Map<String, Double> PValores = new HashMap<String, Double>();

		Double tamanhoTabela = countValores.get(TAMANHO_DA_TABELA);
		Set<String> valores = countValores.keySet();
		valores.remove(TAMANHO_DA_TABELA);
		for (String valor : valores) {
			Double count = countValores.get(valor);
			PValores.put(valor, count / tamanhoTabela);
		}

		return PValores;
	}

	// - - -

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
			countValores.put(TAMANHO_DA_TABELA, Double.valueOf(valores.size()));
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

	private List<Map<String, String>> contagemDetalhada(Map<String, Double> countValores, Map<Integer, String> coluna,
			Integer indice) {
		
		List<Integer> indices = new ArrayList<Integer>();
		
		for (Integer indic : coluna.keySet()) {
			indices.add(indic);
		}
		
		List<Map<String, String>> subConjunto = new ArrayList<Map<String, String>>();
		for (String valor : countValores.keySet()) {
			Integer count = 0;

			for (String classeValor : listaDados.get(indice).values()) {
				Map<String, String> sc = new HashMap<String, String>();
				if (classeValor.equals(valor)) {
					sc.put(valor, listaDados.get(0).get(indices.get(count)));
					subConjunto.add(sc);
				}
				count++;
			}
		}
		return subConjunto;
	}

	private List<List<Map<String, Double>>> countProps(List<Map<String, String>> subConjunto) {
		List<Map<String, Double>> todasFreq = new ArrayList<Map<String, Double>>();
		List<Map<String, Double>> gabarito = new ArrayList<Map<String, Double>>();
		List<List<Map<String, Double>>> retorno = new ArrayList<List<Map<String, Double>>>();

		String stringAnterior = "";

		Integer count = 0;
		Map<String, Double> pValoresProp = new HashMap<String, Double>();
		Integer total = 0;
		pValoresProp.put(TOTAL, total.doubleValue());

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
						pValoresProp.put(TOTAL, total.doubleValue());
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
				pValoresProp.put(TOTAL, total.doubleValue());
			}
		}

		retorno.add(todasFreq);
		retorno.add(gabarito);
		return retorno;
	}

	private Boolean checkSeTudoOk(List<List<Map<String, Double>>> countProps) throws UnsupportedOperationException {

		List<Map<String, Double>> todasFreq = countProps.get(0);
		Double total = 0D;

		for (Map<String, Double> map : todasFreq) {
			if (map.containsKey(TOTAL)) {
				total = total + map.get(TOTAL);
			} else {
				throw new UnsupportedOperationException("Um dos maps não tem total");
			}
		}

		if (total.doubleValue() == dadosCE.get(TAMANHO_DA_TABELA)) {
			return true;
		} else {
			throw new UnsupportedOperationException("Total não é igual ao tabela");
		}
	}

	private void adicionaFrequenciaAPartirDoTotal(List<List<Map<String, Double>>> countProps) {
		List<Map<String, Double>> todasFreq = countProps.get(0);

		for (Map<String, Double> map : todasFreq) {

			Double total = 0D;
			Double freq = 0D;

			if (map.containsKey(TOTAL)) {
				total = map.get(TOTAL);
				freq = total / dadosCE.get(TAMANHO_DA_TABELA);
			}

			map.put(FREQ_DO_TOTAL, freq);
		}
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
			Double totalProp = map.get(TOTAL);
			Double freq = 0D;
			for (String string : map.keySet()) {
				if (!string.equals(TOTAL) && !string.equals(FREQ_DO_TOTAL)) {
					freq = map.get(string) / totalProp;
					freqs.put(prop + "|" + string, freq);
				}
			}

			freqs.put(TOTAL, map.get(TOTAL));
			freqs.put(FREQ_DO_TOTAL, map.get(FREQ_DO_TOTAL));

			todasFreqs.add(freqs);
		}

		return todasFreqs;
	}

	private Map<String, Double> entropiaESomaValoresProps(List<Map<String, Double>> pProps) throws IllegalArgumentException {

		Map<String, Double> entropias = new HashMap<String, Double>();

		Boolean entraDepois = false;

		for (Map<String, Double> map : pProps) {

			Set<String> keySet = map.keySet();
			String prop = "";

			for (String k : keySet) {
				if (!k.equals(TOTAL) && !k.equals(FREQ_DO_TOTAL)) {
					String[] propArr = k.split("\\|");
					prop = propArr[0];
					Double soma = entropiaEsoma(map);
					entropias.put(prop, soma);
				}

				if (entraDepois || k.equals(FREQ_DO_TOTAL)) {
					if (prop.isEmpty()) {
						entraDepois = true;
					} else {
						entropias.put(FREQ_DO_TOTAL + prop, map.get(FREQ_DO_TOTAL));
						entraDepois = false;
					}
				}

			}
		}

		return entropias;
	}

	private List<Double> valoresPropsPonderado(Map<String, Double> entropiaValoresProps) {
		List<Double> ponderados = new ArrayList<Double>();
		for (String string : entropiaValoresProps.keySet()) {
			if (!string.contains(FREQ_DO_TOTAL)) {
				ponderados.add(entropiaValoresProps.get(string) * entropiaValoresProps.get(FREQ_DO_TOTAL + string));
			}
		}
		return ponderados;
	}

	private Double somaPonderado(List<Double> valoresPropsPonderado) {
		Double soma = 0D;

		for (Double d : valoresPropsPonderado) {
			soma = soma + d;
		}

		return soma;
	}

	private Double ganhoDeInformacao(Double somaPonderado) {
		return dadosCE.get(ENTROPIA_DA_TABELA) - somaPonderado;
	}

	// TESTES
	
	public String selecionaPropriedade() {
//		String value = new String();
//		for (String key : props.keySet()) {
//			value = key;
//			break;
//		}
//		return value;
		return null;
	}
	
	public void teste_print() {

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

	public void main() {
		System.out.println(elegeOMaior());
	}
}


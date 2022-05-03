package com.example.demo_proc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo_proc.models.ListasPadrao;
import com.example.demo_proc.models.ModelRequest;
import com.example.demo_proc.models.ModelResponse;
import com.example.demo_proc.models.NohOuRamo;

@Service
@Transactional
public class Processo {

	private static int id;

	public NohOuRamo induzirArvore() {
		ListasPadrao listas = new ListasPadrao();
//		listas.setListaDadosTeste();
		listas.setListaDadosPadrao();
		listas.setPropsPadrao();
		listas.setClassePadrao();
		id = 0;
		int iteracaoAtual = 0;
		
		NohOuRamo retorno = induzirArvore(listas.getListaDados(), listas.getProps(), listas.getClasse(), listas.getClasseFormatado(),
				iteracaoAtual);

		System.out.println("Induzido");
		return retorno;
	}

	public ModelResponse InduzirEPredizer(ModelRequest req) {
		ModelResponse res = new ModelResponse();

		List<Integer> caminho = new ArrayList<Integer>();

		res.setNohFinal(induzirArvore());

		predizer(res.getNohFinal(), req, caminho, res);

		res.setIdCaminho(caminho);

		System.out.println("Predizido");

		return res;
	}
	
	private void predizer(NohOuRamo nor, ModelRequest req, List<Integer> caminho, ModelResponse res) {

		Predicao p = new Predicao();
		p.percorrerPrimeiroNoh(nor, req, caminho, res);
	}

	private NohOuRamo induzirArvore(List<Map<Integer, String>> listaDados, Map<String, Integer> props, String classe,
			String classeFormatada, int iteracaoAtual) {
		iteracaoAtual++;

		NohOuRamo nohRetorno = new NohOuRamo();
		nohRetorno.setClasseNome(classe);
		id++;
		nohRetorno.setId(id);

		String classeValor = casoTodosElementosSejamDaMesmaClasse(listaDados);
		if (!classeValor.isEmpty()) {
			nohRetorno.setValorClasseFolha(classeFormatada + " " + classeValor); 
			return nohRetorno;
		} else if (props.isEmpty()) {
			nohRetorno = norComDisjuncaoDeTodosOsValoresDaClasseDa(listaDados, classe);
			return nohRetorno;
		} else {
			String prop = selecionaUmaPropriedadeP(props, listaDados);
			Integer prop_index = props.get(prop);

			nohRetorno.setNomePropNoh(prop);

			props.remove(prop);

			List<String> listValores = criarListaDeValoresDeUmaProp(listaDados, prop_index);
			for (String valorV : listValores) {

				NohOuRamo ramoRotuladoComV = new NohOuRamo();
				ramoRotuladoComV.setValorPropRamo(valorV);
				ramoRotuladoComV.setClasseNome(classe);
				id++;
				ramoRotuladoComV.setId(id);

				nohRetorno.addFilhos(ramoRotuladoComV);

				List<Map<Integer, String>> particaoV = construirParticaoV(prop_index, valorV,
						listaDados);

				// Para não ser por referência
				Map<String, Integer> propsNovo = new HashMap<String, Integer>();
				propsNovo.putAll(props);

				NohOuRamo nohFilho = new NohOuRamo();

				nohFilho = induzirArvore(particaoV, propsNovo, classe, classeFormatada, iteracaoAtual);
				nohFilho.setClasseNome(classe);
				id++;
				nohFilho.setId(id);

				ramoRotuladoComV.addFilhos(nohFilho);
			}

			return nohRetorno;
		}
	}

	private String casoTodosElementosSejamDaMesmaClasse(List<Map<Integer, String>> listaDados) {
		String valorClasseAnterior = new String();
		int i = 0;
		
		Map<Integer, String> risco_Coluna = listaDados.get(0);

		for (Integer key : risco_Coluna.keySet()) {
			String valorClasse = risco_Coluna.get(key);
			if (!valorClasse.equals(valorClasseAnterior)) {
				valorClasseAnterior = valorClasse;
				i++;
			}
			// Se mais de um for diferente
			if (i > 1) {
				return "";
			}
		}

		// Se todos forem iguais
		return valorClasseAnterior;

	}

	private NohOuRamo norComDisjuncaoDeTodosOsValoresDaClasseDa(List<Map<Integer, String>> listaDados, String classe) {
		Map<Integer, String> risco_Coluna = listaDados.get(0);
		List<String> classeValores = new ArrayList<String>();
		String classeValoresReturned = new String();

		String valueAnterior = "";
		for (String string : risco_Coluna.values()) {
			valueAnterior = string;
			if (!classeValores.contains(valueAnterior)) {
				classeValoresReturned = classeValoresReturned.concat(valueAnterior + " ou ");
				classeValores.add(valueAnterior);
			}
		}

		NohOuRamo novoNoh = new NohOuRamo();
		classeValoresReturned = classeValoresReturned.substring(0, classeValoresReturned.length() - 4);
		novoNoh.setValorClasseFolha(classeValoresReturned + " " + classe);
		return novoNoh;
	}

	private String selecionaUmaPropriedadeP(Map<String, Integer> props, List<Map<Integer, String>> listaDados) {
		Selecao e = new Selecao(props, listaDados);
		
		String prop = e.elegeOMaior();
	
		return prop;
	}

	private List<Map<Integer, String>> construirParticaoV(Integer prop_index, String valor,
			List<Map<Integer, String>> listaDados) {
		
		Map<Integer, String> coluna = listaDados.get(prop_index);
		List<Integer> indices = new ArrayList<Integer>();
		
		for (Integer indice : coluna.keySet()) {
			String value = coluna.get(indice);
			if (value == valor) {
				indices.add(indice);
			}
		}

		List<Map<Integer, String>> particaoVListaDadosCopia = new ArrayList<Map<Integer, String>>();

		for (Map<Integer, String> coluna1 : listaDados) {
			Map<Integer, String> novaColuna = new HashMap<Integer, String>();
			for (Integer indice : indices) {
				novaColuna.put(indice, coluna1.get(indice));
			}
			particaoVListaDadosCopia.add(novaColuna);
		}

		return particaoVListaDadosCopia;
	}

	private List<String> criarListaDeValoresDeUmaProp(List<Map<Integer, String>> listaDados, Integer prop_index) {
		// Cria a lista de valores com valores distintos (únicos)

		List<String> listaValores = new ArrayList<String>();

		Map<Integer, String> prop_Coluna = listaDados.get(prop_index);

		String valueAnterior = "";
		for (String string : prop_Coluna.values()) {
			valueAnterior = string;
			if (!listaValores.contains(valueAnterior)) {
				listaValores.add(valueAnterior);
			}
		}

		return listaValores;

	}
}

package com.example.demo_proc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo_proc.models.ListasPadrao;
import com.example.demo_proc.models.ModelBasicoRequest;
import com.example.demo_proc.models.ModelBasicoResponse;
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

		return induzirArvore(listas.getListaDados(), listas.getProps(), listas.getClasse(), listas.getClasseFormatado(),
				iteracaoAtual);
	}

	public ModelBasicoResponse InduzirEPredizer(ModelBasicoRequest req) {
		ModelBasicoResponse res = new ModelBasicoResponse();

		List<Integer> caminho = new ArrayList<Integer>();

		res.setNohFinal(induzirArvore());

		predizer(res.getNohFinal(), req, caminho, res);

		res.setIdCaminho(caminho);

		return res;
	}

	private NohOuRamo induzirArvore(List<Map<Integer, String>> listaDados, Map<String, Integer> props, String classe,
			String classeFormatada, int iteracaoAtual) {
		iteracaoAtual++;

		// MOVI // MOVI // MOVI !!
		NohOuRamo nohRetorno = new NohOuRamo();
		nohRetorno.setClasseNome(classe);
		id++;
		nohRetorno.setId(id);
		// MOVI // MOVI // MOVI !!

		String classeValor = casoTodosElementosSejamDaMesmaClasse(listaDados);
		if (!classeValor.isEmpty()) {
			nohRetorno.setValorClasseFolha(classeFormatada + " " + classeValor); // CORRIGIDO // CORRIGIDO //
																					// CORRIGIDO!!
			return nohRetorno;
		} else if (props.isEmpty()) {
			nohRetorno = norComDisjuncaoDeTodosOsValoresDaClasseDa(listaDados, classe);
			return nohRetorno;
		} else {
			String prop = selecionaUmaPropriedadeP(props);
			Integer prop_index = props.get(prop);

			nohRetorno.setNomePropNoh(prop);

			props.remove(prop);

			List<String> listValores = criarListaDeValoresDeUmaProp(listaDados, prop_index);
			for (String valorV : listValores) {

				NohOuRamo ramoV = new NohOuRamo();
				ramoV.setValorPropRamo(valorV);
				ramoV.setClasseNome(classe);
				id++;
				ramoV.setId(id);

				// Adiciona aos filhos
				nohRetorno.addFilhos(ramoV);

				List<Map<Integer, String>> particaoVListaDadosCopia = construirParticaoV(prop_index, valorV,
						listaDados);

				// Para não ser por referÃªncia
				Map<String, Integer> propsNovo = new HashMap<String, Integer>();
				propsNovo.putAll(props);

				NohOuRamo nohFilho = new NohOuRamo(); // MOVI // MOVI // MOVI !!

				nohFilho = induzirArvore(particaoVListaDadosCopia, propsNovo, classe, classeFormatada, iteracaoAtual);
				nohFilho.setClasseNome(classe);
				id++;
				nohFilho.setId(id);

				ramoV.addFilhos(nohFilho);
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
			// se mais de um for diferente
			if (i > 1) {
				return "";
			}
		}

		// se todos forem iguais
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
				classeValoresReturned = classeValoresReturned.concat(valueAnterior + ", ");
				classeValores.add(valueAnterior);
			}
		}

//		for (Integer key : risco_Coluna.keySet()) {
//			String value = risco_Coluna.get(key);
//			if (!classeValores.contains(value)) {
//				classeValoresReturned = classeValoresReturned.concat(value + ",");
//				classeValores.add(value);
//			}
//		}

		NohOuRamo novoNoh = new NohOuRamo();
		classeValoresReturned = classeValoresReturned.substring(0, classeValoresReturned.length() - 2);
		novoNoh.setValorClasseFolha(classeValoresReturned + " " + classe);
		return novoNoh;
	}

	private String selecionaUmaPropriedadeP(Map<String, Integer> props) {
		// EH AQUI QUE ENTRA O ENTROPIA!!!!
		Entropia e = new Entropia(props, null);
		
		String value = new String();
		for (String key : props.keySet()) {
			value = key;
			break;
		}
		return value;
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
		// Cria a lista de valores com valores distintos (Ãºnicos)

		List<String> listaValores = new ArrayList<String>();

		Map<Integer, String> prop_Coluna = listaDados.get(prop_index);

		String valueAnterior = "";
		for (String string : prop_Coluna.values()) {
			valueAnterior = string;
			if (!listaValores.contains(valueAnterior)) {
				listaValores.add(valueAnterior);
			}
		}

//		for (Integer valor : prop_Coluna.keySet()) {
//		String value = prop_Coluna.get(valor);
//		if (!listaValores.contains(value)) {
//			listaValores.add(value);
//		}
//	}

		System.out.println(listaValores);
		return listaValores;

	}

	public void predizer(NohOuRamo nor, ModelBasicoRequest req, List<Integer> caminho, ModelBasicoResponse res) {

		percorrerPrimeiroNoh(nor, req, caminho, res);
//		String[] r2 = r.split("\\|");
//		List<String> lr = Arrays.asList(r2);
//		Collections.reverse(lr);
	}

	private String decidirFolha(NohOuRamo nor, ModelBasicoRequest aux, List<Integer> caminho, ModelBasicoResponse res) {
		if (nor.getValorClasseFolha() != null) {
			ListasPadrao listas = new ListasPadrao();
			listas.setClassePadrao();
			caminho.add(nor.getId());
			res.setRiscoFinal(listas.getClasseFormatado() + " " + nor.getValorClasseFolha());
			return nor.getValorClasseFolha();
		} else {
			return null;
		}
	}

	private String decidirNoh(NohOuRamo nor, ModelBasicoRequest aux, List<Integer> caminho) {
		if (nor.getNomePropNoh() != null) {
			caminho.add(nor.getId());
			return nor.getNomePropNoh();
		} else {
			return null;
		}
	}

	private String decidirValorRamo(NohOuRamo nor, ModelBasicoRequest aux, List<Integer> caminho) {
		if (nor.getValorPropRamo() != null) {
			return nor.getValorPropRamo();
		} else {
			return null;
		}
	}

	private String decidirValorComparar(String tipo, ModelBasicoRequest aux) {
		switch (tipo) {
		case "História de Crédito": {
			return aux.getHistoria();
		}
		case "Dívida": {
			return aux.getDivida();
		}
		case "Renda": {
			return aux.getRenda();
		}
		case "Garantia": {
			return aux.getGarantia();
		}
		}
		return null;
	}

	private String percorrerCasoFolhaOuNoh(NohOuRamo nor, ModelBasicoRequest aux, List<Integer> caminho, ModelBasicoResponse res) {
		String nomeFolha = decidirFolha(nor, aux, caminho, res);
		String nomeNoh = decidirNoh(nor, aux, caminho);

		if (nomeFolha != null) {
			return nomeFolha;
		} else if (nomeNoh != null) {
			return percorrerCasoRamoOuNoh(nor, aux, nomeNoh, caminho, res);
		} else {
			return null;
		}
	}

	private String percorrerCasoRamoOuNoh(NohOuRamo norInicial, ModelBasicoRequest aux, String propNome, List<Integer> caminho,
			ModelBasicoResponse res) {
		String retorno = null;
		String valorPropRamoComparar = decidirValorComparar(propNome, aux);
		NohOuRamo ultimoNor = new NohOuRamo();
		boolean encontrado = false;

		if (!norInicial.getFilhos().isEmpty()) {
			for (NohOuRamo norFilho : norInicial.getFilhos()) {
				String valorRamo = decidirValorRamo(norFilho, aux, caminho);

				if (valorRamo != null && valorRamo.equals(valorPropRamoComparar)) {
					caminho.add(norFilho.getId());
					retorno = percorrerCasoFolhaOuNoh(norFilho.getFilhos().get(0), aux, caminho, res);
					encontrado = true;
				}

				ultimoNor = norFilho;
			}
			if (!encontrado) {
				retorno = percorrerCasoFolhaOuNoh(ultimoNor, aux, caminho, res);
			}
		} else {
			throw new UnsupportedOperationException("Não é Ramo nem Noh");
		}

		return retorno;
	}

	private void percorrerPrimeiroNoh(NohOuRamo norInicial, ModelBasicoRequest aux, List<Integer> caminho, ModelBasicoResponse res) {

		if (norInicial.getFilhos().isEmpty()) {
			decidirFolha(norInicial, aux, caminho, res);
		} else {
			String nomeNoh = decidirNoh(norInicial, aux, caminho);
			percorrerCasoRamoOuNoh(norInicial, aux, nomeNoh, caminho, res);
		}
	}
}

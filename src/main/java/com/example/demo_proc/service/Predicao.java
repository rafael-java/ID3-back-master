package com.example.demo_proc.service;

import java.util.List;

import com.example.demo_proc.error_exception.InternalServerErrorException;
import com.example.demo_proc.models.ListasPadrao;
import com.example.demo_proc.models.ModelRequest;
import com.example.demo_proc.models.ModelResponse;
import com.example.demo_proc.models.NohOuRamo;

public class Predicao {

	public void percorrerPrimeiroNoh(NohOuRamo norInicial, ModelRequest aux, List<Integer> caminho, ModelResponse res) {

		if (norInicial.getFilhos().isEmpty()) {
			decidirFolha(norInicial, aux, caminho, res);
		} else {
			String nomeNoh = decidirNoh(norInicial, aux, caminho);
			percorrerCasoRamoOuNoh(norInicial, aux, nomeNoh, caminho, res);
		}
	}
	
	private String percorrerCasoFolhaOuNoh(NohOuRamo nor, ModelRequest aux, List<Integer> caminho, ModelResponse res) {
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

	private String percorrerCasoRamoOuNoh(NohOuRamo norInicial, ModelRequest aux, String propNome, List<Integer> caminho,
			ModelResponse res) {
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
			throw new InternalServerErrorException("Não é Ramo nem Noh, provavelmente nunca vai acontecer de entrar aqui");
		}

		return retorno;
	}
	
	private String decidirFolha(NohOuRamo nor, ModelRequest aux, List<Integer> caminho, ModelResponse res) {
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

	private String decidirNoh(NohOuRamo nor, ModelRequest aux, List<Integer> caminho) {
		if (nor.getNomePropNoh() != null) {
			caminho.add(nor.getId());
			return nor.getNomePropNoh();
		} else {
			return null;
		}
	}

	private String decidirValorRamo(NohOuRamo nor, ModelRequest aux, List<Integer> caminho) {
		if (nor.getValorPropRamo() != null) {
			return nor.getValorPropRamo();
		} else {
			return null;
		}
	}

	private String decidirValorComparar(String tipo, ModelRequest aux) {
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

}

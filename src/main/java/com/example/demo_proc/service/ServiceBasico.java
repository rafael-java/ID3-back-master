//package com.example.demo_proc.service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.security.auth.message.callback.PrivateKeyCallback.Request;
//import javax.validation.Valid;
//
//import org.apache.catalina.connector.Response;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.example.demo_proc.models.ListasPadrao;
//import com.example.demo_proc.models.ModelBasicoRequest;
//import com.example.demo_proc.models.ModelBasicoResponse;
//import com.example.demo_proc.models.NohOuRamo;
//
//@Transactional
//@Service
//public class ServiceBasico {
//
//	public ModelBasicoResponse processa(@Valid ModelBasicoRequest req) throws Exception {
//		ModelBasicoResponse res = new ModelBasicoResponse();
//		//(req.getDivida()+1);
//		//res.setLinha_nova(req.getLinha()+1);
//		//res.setUmBool_novo(!req.getUmBool());
//		///res.setTeste_novo(req.getTeste());
//
//		List<Integer> caminho = new ArrayList<Integer>();
//		caminho.add(4);
//		caminho.add(3);
//		caminho.add(2);
//		
//		res.setIdCaminho(caminho);
//
//		//if (res == null) {
//		//	throw new Exception("Error") ;
//		//}
//		
//		return res;
//	}
//	
//	public ModelBasicoResponse induzir(@Valid ModelBasicoRequest req) throws Exception {
//		ModelBasicoResponse res = new ModelBasicoResponse();
//		
//		Processo p = new Processo();
//		
//		NohOuRamo nr = new NohOuRamo();
//		
//		List<Integer> caminho = new ArrayList<Integer>();
//		caminho.add(4);
//		caminho.add(3);
//		caminho.add(2);
//		
//		nr = p.induzirArvore();
//		
//		res.setIdCaminho(caminho);
//					
//		return res;
//	}
//	
//	public ModelBasicoResponse predizer() throws Exception {
//		ModelBasicoResponse res = new ModelBasicoResponse();
//		
//		Processo p = new Processo();
//		
//		return p.InduzirEPredizer();
//	}
//	
//	public ModelBasicoResponse induzirEpredizer(ModelBasicoRequest req) throws Exception {
//		ModelBasicoResponse res = new ModelBasicoResponse();
//		
//		Processo p = new Processo();
//		
//		res = p.InduzirEPredizer();
//		
//		return res;
//	}
//}

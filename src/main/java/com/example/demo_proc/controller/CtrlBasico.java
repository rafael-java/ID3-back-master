package com.example.demo_proc.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo_proc.models.ModelRequest;
import com.example.demo_proc.models.ModelResponse;
import com.example.demo_proc.models.NohOuRamo;
import com.example.demo_proc.service.Processo;

@RestController
@RequestMapping

public class CtrlBasico {

	@Autowired
	private Processo proc;

	@GetMapping(value = "/v1/induzir", produces = { "application/json", "application/xml" })
	public ResponseEntity<NohOuRamo> metodoInduzir() throws Exception {
		NohOuRamo res = proc.induzirArvore();
		return ResponseEntity.ok(res);
	}

	@PostMapping(value = "/v1/predizer", produces = { "application/json", "application/xml" }, consumes = {
			"application/json", "application/xml" })
	public ResponseEntity<ModelResponse> metodoInduzirEPredizer(@Valid @RequestBody ModelRequest req)
			throws Exception {
		ModelResponse res = proc.InduzirEPredizer(req);
		return ResponseEntity.ok(res);
	}

}

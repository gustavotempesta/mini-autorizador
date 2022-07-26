package br.com.vr.miniautorizador.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vr.miniautorizador.controller.form.TransacaoForm;
import br.com.vr.miniautorizador.model.RespostaTransacao;
import br.com.vr.miniautorizador.model.Transacao;
import br.com.vr.miniautorizador.service.TransacaoService;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
	
	private TransacaoService transacaoService;
	
	@Autowired
	TransacaoController(TransacaoService transacaoService){
		this.transacaoService = transacaoService;
	}
	
	// realizar uma transacao
	@PostMapping
	@Transactional
	public ResponseEntity<String> realizaTransacao(@RequestBody TransacaoForm form){
		Transacao transacao = form.converter();
		try {
			RespostaTransacao resposta = transacaoService.realizaTransacao(transacao);
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(resposta.toString());
		} catch (RuntimeException e) {
			return ResponseEntity
					.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body(e.getMessage());
		}
	}
		
}

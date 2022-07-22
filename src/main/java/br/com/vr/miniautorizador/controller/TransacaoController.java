package br.com.vr.miniautorizador.controller;

import java.math.BigDecimal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vr.miniautorizador.controller.dto.TransacaoDto;
import br.com.vr.miniautorizador.controller.form.TransacaoForm;
import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.model.RespostaTransacao;
import br.com.vr.miniautorizador.model.Transacao;
import br.com.vr.miniautorizador.repository.CartaoRepository;
import br.com.vr.miniautorizador.repository.TransacaoRepository;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	// realizar uma transacao
	@PostMapping
	@Transactional
	public ResponseEntity<RespostaTransacao> realizaTransacao(@RequestBody TransacaoForm form){
		Transacao transacao = form.converter();
		Optional<Cartao> optional = cartaoRepository.findById(transacao.getCartao().getNumero());
		RespostaTransacao resposta = RespostaTransacao.CARTAO_INEXISTENTE;
		try {
			optional.orElseThrow(()-> new RuntimeException());
			Cartao cartao = optional.get();
			if (cartao.getSenha().equals(transacao.getCartao().getSenha())) {
				if(cartao.getSaldo().subtract(transacao.getValor()).compareTo(new BigDecimal(0)) >= 0) {
					transacaoRepository.save(transacao);
					cartao.realizaTransacao(transacao.getValor());
					return ResponseEntity
							.status(HttpStatus.CREATED)
							.body(new TransacaoDto(RespostaTransacao.OK).getResposta());
				} else {
					resposta = RespostaTransacao.SALDO_INSUFICIENTE;
					throw new RuntimeException();
				}
			} else {
				resposta = RespostaTransacao.SENHA_INVALIDA;
				throw new RuntimeException();
			}
		} catch (RuntimeException e) {
			return ResponseEntity
						.status(HttpStatus.UNPROCESSABLE_ENTITY)
						.body(new TransacaoDto(resposta).getResposta());
		}
	}
		
}

package br.com.vr.miniautorizador.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.model.RespostaTransacao;
import br.com.vr.miniautorizador.model.Transacao;
import br.com.vr.miniautorizador.repository.CartaoRepository;
import br.com.vr.miniautorizador.repository.TransacaoRepository;

@Service
public class TransacaoService {

	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private CartaoRepository cartaoRepository;

	public RespostaTransacao realizaTransacao(Transacao transacao) {
		RespostaTransacao respostaFalha;
		Optional<Cartao> optional = cartaoRepository.findById(transacao.getCartao().getNumero());
		if (optional.isPresent()) {
			Cartao cartao = optional.get();
			if (cartao.getSenha().equals(transacao.getCartao().getSenha())) {
				if (cartao.getSaldo().subtract(transacao.getValor()).compareTo(new BigDecimal(0)) >= 0) {
					transacaoRepository.save(transacao);
					cartao.realizaTransacao(transacao.getValor());
					return RespostaTransacao.OK;
				}else{
					respostaFalha = RespostaTransacao.SALDO_INSUFICIENTE;
				}	
			}else {
				respostaFalha = RespostaTransacao.SENHA_INVALIDA;			
			}
		}else {
			respostaFalha = RespostaTransacao.CARTAO_INEXISTENTE;		
		}
		throw new RuntimeException(respostaFalha.toString());
	}
}

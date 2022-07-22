package br.com.vr.miniautorizador.controller.form;

import java.math.BigDecimal;

import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.model.Transacao;

public class TransacaoForm {
	
	private String numeroCartao;
	private String senhaCartao;
	private BigDecimal valor;
	
	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}
	public void setSenhaCartao(String senhaCartao) {
		this.senhaCartao = senhaCartao;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public Transacao converter() {
		Cartao cartao = new Cartao(numeroCartao, senhaCartao);
		return new Transacao(cartao, valor);
	}
}

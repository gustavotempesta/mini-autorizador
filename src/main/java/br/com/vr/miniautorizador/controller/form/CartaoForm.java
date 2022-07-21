package br.com.vr.miniautorizador.controller.form;

import br.com.vr.miniautorizador.model.Cartao;

public class CartaoForm {
	
	private String senha;
	private String numeroCartao;
	
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public Cartao converter() {
		return new Cartao(numeroCartao, senha);
	}	
}

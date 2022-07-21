package br.com.vr.miniautorizador.controller.dto;

import br.com.vr.miniautorizador.model.Cartao;

public class CartaoDto {
	private String senha;
	private String numeroCartao;
	
	public CartaoDto(Cartao cartao) {
		this.senha = cartao.getSenha();
		this.numeroCartao = cartao.getNumero();
	}

	public String getSenha() {
		return senha;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}
	
}

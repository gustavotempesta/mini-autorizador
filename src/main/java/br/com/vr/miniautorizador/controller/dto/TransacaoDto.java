package br.com.vr.miniautorizador.controller.dto;

import br.com.vr.miniautorizador.model.RespostaTransacao;

public class TransacaoDto {

	private RespostaTransacao resposta;

	public TransacaoDto(RespostaTransacao resposta) {
		this.resposta = resposta;
	}
	
	public RespostaTransacao getResposta() {
		return resposta;
	}
	
}

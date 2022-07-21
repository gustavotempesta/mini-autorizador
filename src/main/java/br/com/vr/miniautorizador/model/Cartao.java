package br.com.vr.miniautorizador.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cartoes")
public class Cartao {
	
	@Id
	private String numero;
	private String senha;
	private BigDecimal saldo = new BigDecimal(500);

	public Cartao() {}

	public Cartao(String numero, String senha) {
		this.numero = numero;
		this.senha = senha;
	}
	
	public String getNumero() {
		return numero;
	}

	public String getSenha() {
		return senha;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}
		
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public void realizaTransacao(Transacao transacao) {
		setSaldo(saldo.subtract(transacao.getValor()));
	}
	
}

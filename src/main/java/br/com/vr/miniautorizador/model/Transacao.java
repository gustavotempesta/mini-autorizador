package br.com.vr.miniautorizador.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transacoes")
public class Transacao {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "cartao_numero")
	private Cartao cartao;
	private BigDecimal valor;
	private LocalDateTime dataHora = LocalDateTime.now();
	
	public Transacao() {};
	
	public Transacao(Cartao cartao, BigDecimal valor) {
		this.cartao = cartao;
		this.valor = valor;
	}

	public BigDecimal getValor() {
		return valor;
	}

}

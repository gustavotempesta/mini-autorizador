package br.com.vr.miniautorizador.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.repository.CartaoRepository;

@Service
public class CartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	public void criarCartao(Cartao cartao) {
		Optional<Cartao> optional = cartaoRepository.findById(cartao.getNumero());
		try {
			optional.orElseThrow(()-> new NotFoundException());
			throw new RuntimeException();
		} catch (NotFoundException e) {
			cartaoRepository.save(cartao);
		}
	}
	
	public BigDecimal obterSaldo(String id) {
		Optional<Cartao> optional = cartaoRepository.findById(id);
		optional.orElseThrow(()-> new RuntimeException());
		return optional.get().getSaldo();
	}
}

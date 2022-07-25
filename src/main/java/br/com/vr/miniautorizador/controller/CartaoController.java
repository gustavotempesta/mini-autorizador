package br.com.vr.miniautorizador.controller;

import java.math.BigDecimal;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.vr.miniautorizador.controller.dto.CartaoDto;
import br.com.vr.miniautorizador.controller.form.CartaoForm;
import br.com.vr.miniautorizador.model.Cartao;
import br.com.vr.miniautorizador.service.CartaoService;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
	
	private CartaoService cartaoService;
	
	@Autowired
	CartaoController(CartaoService cartaoService){
		this.cartaoService = cartaoService;
	}
	
	//criar novo cartao
	@PostMapping
	@Transactional
	public ResponseEntity<CartaoDto> criarCartao(@RequestBody @Valid CartaoForm form){
		Cartao cartao = form.converter();
		try {
			cartaoService.criarCartao(cartao);
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(new CartaoDto(cartao));
		} catch (RuntimeException e) {
			return ResponseEntity
					.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body(new CartaoDto(cartao));
		}
	}
	
	//obter saldo do cartao
	@GetMapping("/{id}")
	public ResponseEntity<BigDecimal> obterSaldo(@PathVariable String id) {
		try{
			BigDecimal saldo = cartaoService.obterSaldo(id);
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(saldo);
		} catch (RuntimeException e) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(null);
		}
	}
}

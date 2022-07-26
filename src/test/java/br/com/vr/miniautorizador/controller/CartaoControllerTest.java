package br.com.vr.miniautorizador.controller;

import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.vr.miniautorizador.repository.CartaoRepository;
import br.com.vr.miniautorizador.repository.TransacaoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CartaoControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	@BeforeEach
	void adicionaRegistro() throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(new URI("/cartoes"))
				.content("{\"numeroCartao\":\"6549873025634501\",\"senha\":\"1234\"}")
				.contentType(MediaType.APPLICATION_JSON));
	}
	
	@AfterEach
	void apagaRegistro(){
		transacaoRepository.deleteAll();
		cartaoRepository.deleteAll();
	}
	
	@Test
	void deveriaCadastrarNovoCartaoComSucesso() throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(new URI("/cartoes"))
				.content("{\"numeroCartao\":\"1234567890123456\",\"senha\":\"1234\"}")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(201))
		.andExpect(MockMvcResultMatchers
				.content()
				.json("{\"senha\":\"1234\",\"numeroCartao\":\"1234567890123456\"}"));		
	}
	
	@Test
	void deveriaDevolver400CasoOCartaoCadastradoNaoTenha16Digitos() throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(new URI("/cartoes"))
				.content("{\"numeroCartao\":\"1\",\"senha\":\"1234\"}")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(400))
		.andExpect(MockMvcResultMatchers
				.content()
				.string(""));

		mockMvc
		.perform(MockMvcRequestBuilders
				.post(new URI("/cartoes"))
				.content("{\"numeroCartao\":\"12345678901234567\",\"senha\":\"1234\"}")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(400))
		.andExpect(MockMvcResultMatchers
				.content()
				.string(""));	
	}
	
	@Test
	void deveriaDevolver422CasoOCartaoCadastradoJaExista() throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(new URI("/cartoes"))
				.content("{\"numeroCartao\":\"6549873025634501\",\"senha\":\"1234\"}")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(422))
		.andExpect(MockMvcResultMatchers
				.content()
				.json("{\"senha\":\"1234\",\"numeroCartao\":\"6549873025634501\"}"));		
	}
	
	@Test
	void deveriaDevolverSaldoDoCartao() throws Exception {
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(new URI("/cartoes/6549873025634501"))
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(200))
		.andExpect(MockMvcResultMatchers
				.content()
				.string("500.00"));
	}
	
	@Test
	void deveriaDevolver404CasoOCartaoNaoExista() throws Exception{
		mockMvc
		.perform(MockMvcRequestBuilders
				.get(new URI("/cartoes/1"))
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(404))
		.andExpect(MockMvcResultMatchers
				.content()
				.string(""));
	}
}

package br.com.vr.miniautorizador.controller;

import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.vr.miniautorizador.model.RespostaTransacao;
import br.com.vr.miniautorizador.repository.CartaoRepository;
import br.com.vr.miniautorizador.repository.TransacaoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TransacaoControllerTest {

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
	void deveriaRealizarUmaTransacaoComSucesso() throws Exception{			
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(new URI("/transacoes"))
				.content("{\"numeroCartao\":\"6549873025634501\",\"senhaCartao\":\"1234\",\"valor\":\"10.00\"}")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(201))
		.andExpect(MockMvcResultMatchers
				.content()
				.string(RespostaTransacao.OK.toString()));	
	}
	
	@Test
	void deveriaDevolver422CasoOCartaoNaoExista() throws Exception{
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(new URI("/transacoes"))
				.content("{\"numeroCartao\":\"1\",\"senhaCartao\":\"1234\",\"valor\":\"10.00\"}")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(422))
		.andExpect(MockMvcResultMatchers
				.content()
				.string(RespostaTransacao.CARTAO_INEXISTENTE.toString()));				
	}
	
	@Test
	void deveriaDevolver422CasoASenhaSejaInvalida() throws Exception{
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(new URI("/transacoes"))
				.content("{\"numeroCartao\":\"6549873025634501\",\"senhaCartao\":\"0000\",\"valor\":\"10.00\"}")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(422))
		.andExpect(MockMvcResultMatchers
				.content()
				.string(RespostaTransacao.SENHA_INVALIDA.toString()));				
	}
	
	@Test
	void deveriaDevolver422CasoOSaldoSejaInsuficiente() throws Exception{
		URI uri = new URI("/transacoes");
		String json = "{\"numeroCartao\":\"6549873025634501\",\"senhaCartao\":\"1234\",\"valor\":\"500.00\"}";
		
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(201))
		.andExpect(MockMvcResultMatchers
				.content()
				.string(RespostaTransacao.OK.toString()));
		
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(422))
		.andExpect(MockMvcResultMatchers
				.content()
				.string(RespostaTransacao.SALDO_INSUFICIENTE.toString()));
	}

}

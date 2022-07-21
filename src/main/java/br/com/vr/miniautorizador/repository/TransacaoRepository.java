package br.com.vr.miniautorizador.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.vr.miniautorizador.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long>{

}

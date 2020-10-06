package com.fiances.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiances.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}

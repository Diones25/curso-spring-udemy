package com.example.produtosapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.produtosapi.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, String> {
  
}

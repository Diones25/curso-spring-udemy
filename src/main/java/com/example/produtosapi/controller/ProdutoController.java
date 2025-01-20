package com.example.produtosapi.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.produtosapi.model.Produto;
import com.example.produtosapi.repository.ProdutoRepository;

@RestController
@RequestMapping("produtos")
public class ProdutoController {

  private ProdutoRepository produtoRepository;

  public ProdutoController(ProdutoRepository produtoRepository) {
    this.produtoRepository = produtoRepository;
  }
  
  @PostMapping
  public Produto salvar(@RequestBody Produto produto) {
    System.out.println("Produto salvo com sucesso! " + produto);

    var id = UUID.randomUUID().toString();
    produto.setId(id);

    produtoRepository.save(produto);
    return produto;
  }
  
  @GetMapping
  public List<Produto> listar() {
    var produto = produtoRepository.findAll();
    return produto;
  }
}

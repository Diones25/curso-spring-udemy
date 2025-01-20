package com.example.produtosapi.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    var produtos = produtoRepository.findAll();
    return produtos;
  }

  @GetMapping("/{id}")
  public Produto obterProdutoPorId(@PathVariable("id") String id) {
    Optional<Produto> produto = produtoRepository.findById(id);
    return produto.isPresent() ? produto.get() : null;
  }

  @DeleteMapping("/{id}")
  public void deletarProduto(@PathVariable("id") String id) {
    produtoRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  public void atualizar(@PathVariable("id") String id, @RequestBody Produto produto) {
    produto.setId(id);
    produtoRepository.save(produto);
  }
}

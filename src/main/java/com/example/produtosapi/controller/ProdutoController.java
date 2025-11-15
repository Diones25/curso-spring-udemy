package com.example.produtosapi.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    var id = UUID.randomUUID().toString();
    produto.setId(id);

    produtoRepository.save(produto);
    return produto;
  }

  @GetMapping("/{id}")
  public Produto obterProdutoPorId(@PathVariable("id") String id) {
    return produtoRepository.findById(id).orElse(null);
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

  @GetMapping
  public ResponseEntity<List<Produto>> buscar(@RequestParam(value = "nome", required = false) String nome) {
    List<Produto> produtos;

    if (nome == null || nome.trim().isEmpty()) {
      produtos = produtoRepository.findAll();
    }
    else {
      produtos = produtoRepository.findByNome(nome);
    }
    
    if(produtos.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok(produtos);
  }
}

package com.example.produtosapi.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

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
  public ResponseEntity<Produto> salvar(@RequestBody Produto produto) {
    var id = UUID.randomUUID().toString();
    produto.setId(id);

    produtoRepository.save(produto);
    return ResponseEntity.status(201).body(produto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Produto> obterProdutoPorId(@PathVariable("id") String id) {
    Optional<Produto> produtoOpt = produtoRepository.findById(id);

    if (produtoOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(produtoOpt.get());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletarProduto(@PathVariable("id") String id) {
    produtoRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> atualizar(@PathVariable("id") String id, @RequestBody Produto produto) {
    // Verificando se o id do produto existe
    produtoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    produto.setId(id);
    produtoRepository.save(produto);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<Produto>> buscar(@RequestParam(value = "nome", required = false) String nome) {
    List<Produto> produtos;

    if (nome == null || nome.trim().isEmpty()) {
      produtos = produtoRepository.findAll();
    } else {
      produtos = produtoRepository.findByNome(nome);
    }

    if (produtos.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(produtos);
  }
}

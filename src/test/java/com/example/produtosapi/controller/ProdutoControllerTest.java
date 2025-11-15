package com.example.produtosapi.controller;

import com.example.produtosapi.model.Produto;
import com.example.produtosapi.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ProdutoRepository produtoRepository;

  private Produto produto;

  @BeforeEach
  void setUp() {
    produto = new Produto();
    produto.setId(UUID.randomUUID().toString());
    produto.setNome("Notebook");
    produto.setDescricao("Notebook de última geração");
    produto.setPreco(5000.00); // O tipo em Produto.java é Double
  }

  @Test
  void quandoSalvar_entaoRetornaStatusCreatedEProduto() throws Exception {
    // Arrange
    Produto produtoParaSalvar = new Produto();
    produtoParaSalvar.setNome("Notebook");
    produtoParaSalvar.setDescricao("Notebook de última geração");
    produtoParaSalvar.setPreco(5000.00);

    when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

    // Act & Assert
    mockMvc.perform(post("/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(produtoParaSalvar)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.nome").value("Notebook"));
  }

  @Test
  void quandoObterProdutoPorIdExistente_entaoRetornaStatusOkEProduto() throws Exception {
    // Arrange
    when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

    // Act & Assert
    mockMvc.perform(get("/produtos/{id}", produto.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(produto.getId()))
        .andExpect(jsonPath("$.nome").value(produto.getNome()));
  }

  @Test
  void quandoObterProdutoPorIdInexistente_entaoRetornaStatusNotFound() throws Exception {
    // Arrange
    when(produtoRepository.findById(anyString())).thenReturn(Optional.empty());

    // Act & Assert
    mockMvc.perform(get("/produtos/{id}", "id-invalido"))
        .andExpect(status().isNotFound());
  }

  @Test
  void quandoDeletarProduto_entaoRetornaStatusNoContent() throws Exception {
    // Arrange
    doNothing().when(produtoRepository).deleteById(produto.getId());

    // Act & Assert
    mockMvc.perform(delete("/produtos/{id}", produto.getId()))
        .andExpect(status().isNoContent());
  }

  @Test
  void quandoAtualizar_entaoRetornaStatusNoContent() throws Exception {
    // Arrange
    // 1. Mock para a verificação de existência do produto
    when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
    when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

    // Act & Assert
    mockMvc.perform(put("/produtos/{id}", produto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(produto))) // 2. Usar o objectMapper injetado
        .andExpect(status().isNoContent());
  }

  @Test
  void quandoBuscarSemParametroNome_entaoRetornaTodosOsProdutos() throws Exception {
    // Arrange
    List<Produto> produtos = List.of(produto);
    when(produtoRepository.findAll()).thenReturn(produtos);

    // Act & Assert
    mockMvc.perform(get("/produtos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].nome").value(produto.getNome()));
  }

  @Test
  void quandoBuscarComParametroNome_entaoRetornaProdutosFiltrados() throws Exception {
    // Arrange
    List<Produto> produtos = List.of(produto);
    when(produtoRepository.findByNome("Notebook")).thenReturn(produtos);

    // Act & Assert
    mockMvc.perform(get("/produtos")
        .param("nome", "Notebook"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].nome").value(produto.getNome()));
  }

  @Test
  void quandoBuscarComParametroNomeVazio_entaoRetornaTodosOsProdutos() throws Exception {
    // Arrange
    List<Produto> produtos = List.of(produto);
    when(produtoRepository.findAll()).thenReturn(produtos);

    // Act & Assert
    mockMvc.perform(get("/produtos")
        .param("nome", " "))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].nome").value(produto.getNome()));
  }

  @Test
  void quandoBuscarEListaVazia_entaoRetornaStatusNotFound() throws Exception {
    // Arrange
    when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

    // Act & Assert
    mockMvc.perform(get("/produtos"))
        .andExpect(status().isNotFound());
  }

  @Test
  void quandoBuscarPorNomeENaoEncontra_entaoRetornaStatusNotFound() throws Exception {
    // Arrange
    when(produtoRepository.findByNome(anyString())).thenReturn(Collections.emptyList());

    // Act & Assert
    mockMvc.perform(get("/produtos")
        .param("nome", "nome-inexistente"))
        .andExpect(status().isNotFound());
  }
}

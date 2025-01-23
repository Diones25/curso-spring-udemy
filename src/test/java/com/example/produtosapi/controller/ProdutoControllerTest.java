package com.example.produtosapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.produtosapi.model.Produto;
import com.example.produtosapi.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {
  
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProdutoRepository produtoRepository;

  @InjectMocks
  private ProdutoController produtoController;

  private Produto produto;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    produto = new Produto();
    produto.setId(UUID.randomUUID().toString());
    produto.setNome("Produto Teste");
  }

  @Test
  public void testSalvar() throws Exception {
    when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

    mockMvc.perform(post("/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(produto)))
        .andExpect(status().isOk())
        //.andExpect(jsonPath("$.id").value(produto.getId()))
        .andExpect(jsonPath("$.nome").value(produto.getNome()));
  }

  @Test
  public void testObterProdutoPorId() throws Exception {
    when(produtoRepository.findById(anyString())).thenReturn(Optional.of(produto));

    mockMvc.perform(get("/produtos/{id}", produto.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(produto.getId()))
        .andExpect(jsonPath("$.nome").value(produto.getNome()));
  }

  @Test
  public void testDeletarProduto() throws Exception {
    mockMvc.perform(delete("/produtos/{id}", produto.getId()))
        .andExpect(status().isOk());
  }

  @Test
  public void testAtualizar() throws Exception {
    when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

    mockMvc.perform(put("/produtos/{id}", produto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(produto)))
        .andExpect(status().isOk());
  }

  @Test
  public void testBuscar() throws Exception {
    when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto));
    when(produtoRepository.findByNome(anyString())).thenReturn(Arrays.asList(produto));

    mockMvc.perform(get("/produtos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(produto.getId()))
        .andExpect(jsonPath("$[0].nome").value(produto.getNome()));

    mockMvc.perform(get("/produtos").param("nome", "Produto Teste"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(produto.getId()))
        .andExpect(jsonPath("$[0].nome").value(produto.getNome()));
  }

  @Test
  public void testBuscarSemNome() throws Exception {
    when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto));

    mockMvc.perform(get("/produtos"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(produto.getId()))
      .andExpect(jsonPath("$[0].nome").value(produto.getNome()));
  }

  @Test
  public void testBuscarComNome() throws Exception {
    when(produtoRepository.findByNome(anyString())).thenReturn(Arrays.asList(produto));

    mockMvc.perform(get("/produtos").param("nome", "Produto Teste"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(produto.getId()))
      .andExpect(jsonPath("$[0].nome").value(produto.getNome()));
  }

  @Test
  public void testBuscarSemResultados() throws Exception {
    when(produtoRepository.findAll()).thenReturn(Arrays.asList());
    when(produtoRepository.findByNome(anyString())).thenReturn(Arrays.asList());

    mockMvc.perform(get("/produtos"))
      .andExpect(status().isNotFound());

    mockMvc.perform(get("/produtos").param("nome", "Produto Inexistente"))
      .andExpect(status().isNotFound());
  }
}

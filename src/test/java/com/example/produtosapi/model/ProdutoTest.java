package com.example.produtosapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProdutoTest {
  private Produto produto;

  @BeforeEach
  public void setUp() {
    produto = new Produto();
    produto.setId("123");
    produto.setNome("Produto Teste");
    produto.setDescricao("Descrição do Produto Teste");
    produto.setPreco(99.99);
  }

  @Test
  public void testGetSetId() {
    String id = "123";
    produto.setId(id);
    assertEquals(id, produto.getId());
  }

  @Test
  public void testGetSetNome() {
    String nome = "Produto Teste";
    produto.setNome(nome);
    assertEquals(nome, produto.getNome());
  }

  @Test
  public void testGetSetDescricao() {
    String descricao = "Descrição do Produto Teste";
    produto.setDescricao(descricao);
    assertEquals(descricao, produto.getDescricao());
  }

  @Test
  public void testGetSetPreco() {
    Double preco = 99.99;
    produto.setPreco(preco);
    assertEquals(preco, produto.getPreco());
  }

  @Test
  public void testToString() {
    String expected = "Produto [id=123, nome=Produto Teste, descricao=Descrição do Produto Teste, preco=99.99]";
    assertEquals(expected, produto.toString());
  }
}

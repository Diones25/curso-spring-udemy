
package com.example.produtosapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProdutoRequestDTO {

  @NotBlank(message = "O nome não pode ser vazio ou nulo.")
  @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
  private String nome;

  private String descricao;

  @NotNull(message = "O preço não pode ser nulo.")
  @Positive(message = "O preço deve ser um valor positivo.")
  private Double preco;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Double getPreco() {
    return preco;
  }

  public void setPreco(Double preco) {
    this.preco = preco;
  }

}
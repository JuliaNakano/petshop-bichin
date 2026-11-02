package model;

import java.util.Objects;

/**
 * Representa um serviço oferecido pelo petshop (banho, tosa, consulta etc.).
 * Um serviço existe independentemente de ter sido prestado ou não (agregação).
 */
public class Servico {

    private int id;
    private String descricao;
    private double valor;

    // Construtor vazio
    public Servico() {
    }

    // Construtor completo
    public Servico(int id, String descricao, double valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    // Dois serviços são iguais quando possuem o mesmo id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servico servico = (Servico) o;
        return id == servico.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Serviço %d - %s (R$ %.2f)", id, descricao, valor);
    }
}
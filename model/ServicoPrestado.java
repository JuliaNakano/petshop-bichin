package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Representa um serviço prestado a um pet em uma determinada data.
 * O serviço prestado depende de um Pet e de um Servico (associação),
 * mas o Pet e o Servico existem sem ele (agregação).
 */
public class ServicoPrestado {

    // atributos
    private int id;
    private Pet pet;
    private Servico servico;
    private double valorCobrado;
    private LocalDate data;

    // construtor vazio
    public ServicoPrestado() {
    }

    // construtor completo
    public ServicoPrestado(int id, Pet pet, Servico servico, double valorCobrado, LocalDate data) {
        this.id = id;
        this.pet = pet;
        this.servico = servico;
        this.valorCobrado = valorCobrado;
        this.data = data;
    }

    // calcula quantos dias se passaram desde a execução do serviço
    public int getDiasDaExecucaoDoServico() {
        if (data == null) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(data, LocalDate.now());
    }

    // metodos get
    public int getId() {
        return id;
    }

    public Pet getPet() {
        return pet;
    }

    public Servico getServico() {
        return servico;
    }

    public double getValorCobrado() {
        return valorCobrado;
    }

    public LocalDate getData() {
        return data;
    }

    // metodos set
    public void setId(int id) {
        this.id = id;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public void setValorCobrado(double valorCobrado) {
        this.valorCobrado = valorCobrado;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // verifica se os objetos associados existem antes de usá-los
        String nomePet = (pet != null) ? pet.getNome() : "Não informado";
        String descricaoServico = (servico != null) ? servico.getDescricao() : "Não informado";
        String dataFormatada = (data != null) ? data.format(formato) : "Não informada";

        return "Serviço prestado: " + id
                + "\n Pet: " + nomePet
                + "\n Serviço: " + descricaoServico
                + "\n Valor cobrado: R$ " + String.format("%.2f", valorCobrado)
                + "\n Data: " + dataFormatada;
    }

    // dois serviços prestados são iguais quando possuem o mesmo id
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServicoPrestado)) {
            return false;
        }
        ServicoPrestado outro = (ServicoPrestado) obj;
        return this.id == outro.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
package controller;

import model.Cliente;
import model.Pet;
import repository.ClienteRepository;
import repository.PetRepository;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Camada de controle dos pets.
 * Recebe os repositories pelo construtor e repassa as solicitações da visão.
 */
public class PetController {

    private PetRepository repository;
    private ClienteRepository repositoryCliente;

    public PetController(PetRepository repository, ClienteRepository repositoryCliente) {
        this.repository = repository;
        this.repositoryCliente = repositoryCliente;
    }

    // um pet só existe com um cliente (composição): o cliente precisa já estar cadastrado
    public void adicionar(int id, String nome, String especie, String raca,
                          LocalDate nascimento, int idCliente) {
        if (repository.buscarPorId(id) != null) {
            throw new IllegalArgumentException("Já existe um pet com esse código.");
        }

        Cliente cliente = repositoryCliente.buscarPorId(idCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        repository.adicionar(new Pet(id, nome, especie, raca, cliente, nascimento));
    }

    public Pet buscar(int id) {
        return repository.buscarPorId(id);
    }

    public ArrayList<Pet> listar() {
        return repository.listar();
    }

    public boolean alterar(int id, String nome, String especie, String raca,
                           LocalDate nascimento, int idCliente) {
        Cliente cliente = repositoryCliente.buscarPorId(idCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        return repository.alterar(id, new Pet(id, nome, especie, raca, cliente, nascimento));
    }

    public boolean remover(int id) {
        return repository.remover(id);
    }
}

package controller;

import model.Cliente;
import repository.ClienteRepository;

import java.util.ArrayList;

/**
 * Camada de controle dos clientes.
 * Recebe o repository pelo construtor e repassa as solicitações da visão.
 */
public class ClienteController {

    private ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    // o código do cliente é gerado automaticamente; retorna o cliente criado
    public Cliente adicionar(String nome, String telefone) {
        Cliente cliente = new Cliente(repository.gerarId(), nome, telefone);
        repository.adicionar(cliente);
        return cliente;
    }

    public Cliente buscar(int id) {
        return repository.buscarPorId(id);
    }

    public ArrayList<Cliente> listar() {
        return repository.listar();
    }

    public boolean alterar(int id, String nome, String telefone) {
        return repository.alterar(id, new Cliente(id, nome, telefone));
    }

    public boolean remover(int id) {
        return repository.remover(id);
    }
}
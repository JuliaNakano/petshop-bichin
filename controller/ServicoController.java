package controller;



import model.Servico;
import repository.ServicoRepository;

import java.util.ArrayList;

/**
 * Camada de controle dos serviços.
 * Recebe o repository pelo construtor e repassa as solicitações da visão.
 */
public class ServicoController {

    private ServicoRepository repository;

    public ServicoController(ServicoRepository repository) {
        this.repository = repository;
    }

    public void adicionar(int id, String descricao, double valor) {
        repository.adicionar(new Servico(id, descricao, valor));
    }

    public Servico buscar(int id) {
        return repository.buscarPorId(id);
    }

    public ArrayList<Servico> listar() {
        return repository.listar();
    }

    public boolean alterar(int id, String descricao, double valor) {
        return repository.alterar(id, new Servico(id, descricao, valor));
    }

    public boolean remover(int id) {
        return repository.remover(id);
    }
}

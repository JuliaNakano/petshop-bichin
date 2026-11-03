package controller;

import model.Cliente;
import model.Pet;
import model.Servico;
import model.ServicoPrestado;
import repository.PetRepository;
import repository.ServicoPrestadoRepository;
import repository.ServicoRepository;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Camada de controle dos serviços prestados.
 * Recebe os repositories pelo construtor e repassa as solicitações da visão.
 */
public class ServicoPrestadoController {

    private ServicoPrestadoRepository repository;
    private PetRepository repositoryPet;
    private ServicoRepository repositoryServico;

    public ServicoPrestadoController(ServicoPrestadoRepository repository, PetRepository repositoryPet,ServicoRepository repositoryServico) {
        this.repository = repository;
        this.repositoryPet = repositoryPet;
        this.repositoryServico = repositoryServico;
    }

    // monta o serviço prestado a partir dos ids informados e o adiciona
    public void adicionar(int id, int idPet, int idServico, double valorCobrado, LocalDate data) {
        if (repository.buscarPorId(id) != null) {
            throw new IllegalArgumentException("Já existe um serviço prestado com esse código.");
        }

        Pet pet = repositoryPet.buscarPorId(idPet);
        if (pet == null) {
            throw new IllegalArgumentException("Pet não encontrado.");
        }

        Servico servico = repositoryServico.buscarPorId(idServico);
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado.");
        }

        repository.adicionar(new ServicoPrestado(id, pet, servico, valorCobrado, data));
    }

    public ServicoPrestado buscar(int id) {
        return repository.buscarPorId(id);
    }

    public ArrayList<ServicoPrestado> listar() {
        return repository.listar();
    }

    public boolean removerServicoPrestado(int id) {
        return repository.remover(id);
    }

    public ArrayList<ServicoPrestado> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.buscarPorPeriodo(inicio, fim);
    }

    public ArrayList<ServicoPrestado> buscarPorCliente(Cliente cliente) {
        return repository.buscarPorCliente(cliente);
    }

    public double maiorValorPorCliente(Cliente cliente) {
        return repository.maiorValorPorCliente(cliente);
    }

    public ArrayList<Pet> buscarBichosPorNomeDono(String nomeDono) {
        return repository.buscarBichosPorNomeDono(nomeDono);
    }
}
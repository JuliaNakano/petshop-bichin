package repository;

import model.Cliente;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Camada de persistência dos clientes.
 * Nesta versão os dados ficam armazenados em um ArrayList.
 */
public class ClienteRepository {

    private ArrayList<Cliente> clientes;

    public ClienteRepository() {
        // o construtor instancia a estrutura de dados interna
        this.clientes = new ArrayList<>();
    }

    public void adicionar(Cliente cliente) {
        clientes.add(cliente);
    }

    // retorna o cliente com o id informado ou null caso não exista
    public Cliente buscarPorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    public ArrayList<Cliente> listar() {
        return clientes;
    }

    // substitui o cliente de id informado. Retorna false se não encontrou
    public boolean alterar(int id, Cliente novo) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == id) {
                novo.setId(id);
                clientes.set(i, novo);
                return true;
            }
        }
        return false;
    }

    // remove o cliente de id informado. Retorna false se não encontrou.
    // usa removeIf com o id para não chamar o equals() de Cliente
    public boolean remover(int id) {
        return clientes.removeIf(cliente -> cliente.getId() == id);
    }

    /**
     * Carrega os clientes iniciais a partir de um arquivo texto.
     * Formato de cada linha: id;nome;telefone (a primeira linha é o cabeçalho).
     */
    public void carregarDados(String caminho) {
        InputStream entrada = getClass().getClassLoader().getResourceAsStream(caminho);

        try {
            if (entrada == null) {
                entrada = new FileInputStream(caminho);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de clientes não encontrado: " + caminho);
            return;
        }

        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(entrada, StandardCharsets.UTF_8))) {

            String linha = leitor.readLine(); // ignora o cabeçalho

            while ((linha = leitor.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] campos = linha.split(";");
                try {
                    int id = Integer.parseInt(campos[0].trim());
                    String nome = campos[1].trim();
                    String telefone = campos[2].trim();
                    clientes.add(new Cliente(id, nome, telefone));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Linha inválida ignorada: " + linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de clientes: " + e.getMessage());
        }
    }
}

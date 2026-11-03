package repository;

import model.Cliente;
import model.Pet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Camada de persistência dos pets.
 * Nesta versão os dados ficam armazenados em um ArrayList.
 */
public class PetRepository {

    private ArrayList<Pet> pets;

    public PetRepository() {
        // o construtor instancia a estrutura de dados interna
        this.pets = new ArrayList<>();
    }

    public void adicionar(Pet pet) {
        pets.add(pet);
    }

    // retorna o pet com o id informado ou null caso não exista
    public Pet buscarPorId(int id) {
        for (Pet pet : pets) {
            if (pet.getId() == id) {
                return pet;
            }
        }
        return null;
    }

    public ArrayList<Pet> listar() {
        return pets;
    }

    // substitui o pet de id informado. Retorna false se não encontrou
    public boolean alterar(int id, Pet novo) {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId() == id) {
                novo.setId(id);
                pets.set(i, novo);
                return true;
            }
        }
        return false;
    }

    // remove o pet de id informado. Retorna false se não encontrou
    public boolean remover(int id) {
        Pet pet = buscarPorId(id);
        if (pet == null) {
            return false;
        }
        return pets.remove(pet);
    }

    /**
     * Carrega os pets iniciais a partir de um arquivo texto.
     * Formato de cada linha: id;nome;especie;raca;nascimento (dd/MM/yyyy);idCliente
     * A primeira linha é o cabeçalho. Os clientes precisam já ter sido carregados.
     */
    public void carregarDados(String caminho, ClienteRepository clienteRepository) {
        InputStream entrada = getClass().getClassLoader().getResourceAsStream(caminho);

        try {
            if (entrada == null) {
                entrada = new FileInputStream(caminho);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de pets não encontrado: " + caminho);
            return;
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
                    String especie = campos[2].trim();
                    String raca = campos[3].trim();
                    LocalDate nascimento = LocalDate.parse(campos[4].trim(), formato);
                    int idCliente = Integer.parseInt(campos[5].trim());

                    Cliente cliente = clienteRepository.buscarPorId(idCliente);
                    if (cliente == null) {
                        System.out.println("Cliente inexistente, linha ignorada: " + linha);
                        continue;
                    }
                    // o construtor de Pet recebe o cliente antes do nascimento
                    pets.add(new Pet(id, nome, especie, raca, cliente, nascimento));

                } catch (NumberFormatException | ArrayIndexOutOfBoundsException
                         | DateTimeParseException e) {
                    System.out.println("Linha inválida ignorada: " + linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de pets: " + e.getMessage());
        }
    }
}

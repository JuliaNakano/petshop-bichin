package repository;

import model.Cliente;
import model.Pet;
import model.Servico;
import model.ServicoPrestado;

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
 * Camada de persistência dos serviços prestados.
 * Nesta versão os dados ficam armazenados em um ArrayList.
 */
public class ServicoPrestadoRepository {

    private ArrayList<ServicoPrestado> servicosPrestados;

    public ServicoPrestadoRepository() {
        // o construtor instancia a estrutura de dados interna
        this.servicosPrestados = new ArrayList<>();
    }

    public void adicionar(ServicoPrestado servicoPrestado) {
        servicosPrestados.add(servicoPrestado);
    }

    public ArrayList<ServicoPrestado> listar() {
        return servicosPrestados;
    }

    // retorna o serviço prestado com o id informado ou null caso não exista
    public ServicoPrestado buscarPorId(int id) {
        for (ServicoPrestado sp : servicosPrestados) {
            if (sp.getId() == id) {
                return sp;
            }
        }
        return null;
    }

    // substitui o serviço prestado de id informado. Retorna false se não encontrou
    public boolean alterar(int id, ServicoPrestado novo) {
        for (int i = 0; i < servicosPrestados.size(); i++) {
            if (servicosPrestados.get(i).getId() == id) {
                novo.setId(id);
                servicosPrestados.set(i, novo);
                return true;
            }
        }
        return false;
    }

    // remove o serviço prestado de id informado. Retorna false se não encontrou
    public boolean remover(int id) {
        ServicoPrestado sp = buscarPorId(id);
        if (sp == null) {
            return false;
        }
        return servicosPrestados.remove(sp);
    }

    // retorna os serviços prestados entre as duas datas (incluindo as extremidades)
    public ArrayList<ServicoPrestado> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        ArrayList<ServicoPrestado> resultado = new ArrayList<>();
        for (ServicoPrestado sp : servicosPrestados) {
            LocalDate data = sp.getData();
            if (data != null && !data.isBefore(inicio) && !data.isAfter(fim)) {
                resultado.add(sp);
            }
        }
        return resultado;
    }

    // retorna os serviços prestados aos pets do cliente informado
    public ArrayList<ServicoPrestado> buscarPorCliente(Cliente cliente) {
        ArrayList<ServicoPrestado> resultado = new ArrayList<>();
        for (ServicoPrestado sp : servicosPrestados) {
            if (pertenceAoCliente(sp, cliente)) {
                resultado.add(sp);
            }
        }
        return resultado;
    }

    // retorna o maior valor cobrado em um serviço dos pets do cliente (0 se não houver)
    public double maiorValorPorCliente(Cliente cliente) {
        double maior = 0;
        for (ServicoPrestado sp : servicosPrestados) {
            if (pertenceAoCliente(sp, cliente) && sp.getValorCobrado() > maior) {
                maior = sp.getValorCobrado();
            }
        }
        return maior;
    }

    // retorna os pets atendidos (sem repetição) cujo dono tem o nome informado
    public ArrayList<Pet> buscarBichosPorNomeDono(String nomeDono) {
        ArrayList<Pet> bichos = new ArrayList<>();
        String busca = nomeDono.trim().toLowerCase();

        for (ServicoPrestado sp : servicosPrestados) {
            Pet pet = sp.getPet();
            if (pet == null || pet.getCliente() == null || pet.getCliente().getNome() == null) {
                continue;
            }
            boolean donoCorresponde = pet.getCliente().getNome().toLowerCase().contains(busca);
            if (donoCorresponde && !bichos.contains(pet)) {
                bichos.add(pet);
            }
        }
        return bichos;
    }

    // verifica se o pet do serviço pertence ao cliente (compara pelo id)
    private boolean pertenceAoCliente(ServicoPrestado sp, Cliente cliente) {
        return sp.getPet() != null
                && sp.getPet().getCliente() != null
                && cliente != null
                && sp.getPet().getCliente().getId() == cliente.getId();
    }

    /**
     * Carrega os serviços prestados iniciais a partir de um arquivo texto.
     * Formato de cada linha: id;idPet;idServico;valorCobrado;data (dd/MM/yyyy).
     * A primeira linha é o cabeçalho. Os pets e serviços precisam já ter sido carregados.
     */
    


    public void carregarDados(String caminho, PetRepository petRepository,ServicoRepository servicoRepository) {

        InputStream entrada = getClass().getClassLoader().getResourceAsStream(caminho);

        try {
            if (entrada == null) {
                entrada = new FileInputStream(caminho);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de serviços prestados não encontrado: " + caminho);
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
                    int idPet = Integer.parseInt(campos[1].trim());
                    int idServico = Integer.parseInt(campos[2].trim());
                    double valor = Double.parseDouble(campos[3].trim().replace(",", "."));
                    LocalDate data = LocalDate.parse(campos[4].trim(), formato);

                    Pet pet = petRepository.buscarPorId(idPet);
                    Servico servico = servicoRepository.buscarPorId(idServico);

                    if (pet == null || servico == null) {
                        System.out.println("Pet ou serviço inexistente, linha ignorada: " + linha);
                        continue;
                    }
                    servicosPrestados.add(new ServicoPrestado(id, pet, servico, valor, data));

                } catch (NumberFormatException | ArrayIndexOutOfBoundsException
                         | DateTimeParseException e) {
                    System.out.println("Linha inválida ignorada: " + linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de serviços prestados: " + e.getMessage());
        }
    }
        
}
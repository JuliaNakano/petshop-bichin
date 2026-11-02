package repository;

import model.Servico;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Camada de persistência dos serviços.
 * Nesta versão os dados ficam armazenados em um ArrayList.
 */
public class ServicoRepository {

    private ArrayList<Servico> servicos;

    public ServicoRepository() {
        // O construtor instancia a estrutura de dados interna
        this.servicos = new ArrayList<>();
    }

    public void adicionar(Servico servico) {
        servicos.add(servico);
    }

    // Retorna o serviço com o id informado ou null caso não exista
    public Servico buscarPorId(int id) {
        for (Servico servico : servicos) {
            if (servico.getId() == id) {
                return servico;
            }
        }
        return null;
    }

    public ArrayList<Servico> listar() {
        return servicos;
    }

    // Substitui os dados do serviço de id informado. Retorna false se não encontrou.
    public boolean alterar(int id, Servico novo) {
        for (int i = 0; i < servicos.size(); i++) {
            if (servicos.get(i).getId() == id) {
                novo.setId(id);
                servicos.set(i, novo);
                return true;
            }
        }
        return false;
    }

    // Remove o serviço de id informado. Retorna false se não encontrou.
    public boolean remover(int id) {
        Servico servico = buscarPorId(id);
        if (servico == null) {
            return false;
        }
        return servicos.remove(servico);
    }

    /**
     * Carrega os serviços iniciais a partir de um arquivo texto.
     * Formato de cada linha: id;descricao;valor (a primeira linha é o cabeçalho).
     * O arquivo é procurado primeiro em resources e depois no sistema de arquivos.
     */
    public void carregarDados(String caminho) {
        InputStream entrada = getClass().getClassLoader().getResourceAsStream(caminho);

        try {
            if (entrada == null) {
                entrada = new FileInputStream(caminho);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de serviços não encontrado: " + caminho);
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
                    String descricao = campos[1].trim();
                    double valor = Double.parseDouble(campos[2].trim().replace(",", "."));
                    servicos.add(new Servico(id, descricao, valor));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Linha inválida ignorada: " + linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de serviços: " + e.getMessage());
        }
    }
}
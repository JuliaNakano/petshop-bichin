package view;


import controller.ServicoController;
import model.Servico;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Interface em linha de comando para o cadastro de serviços.
 * Apenas iniciar() é público; as demais operações são privadas.
 */
public class UiCmdServico {

    private Scanner teclado;
    private ServicoController controller;

    public UiCmdServico(ServicoController controller) {
        this.teclado = new Scanner(System.in);
        this.controller = controller;
    }

    // Mostra o menu de serviços em um laço até o usuário escolher voltar
    public void iniciar() {
        int opcao;
        do {
            System.out.println("\n===== SERVIÇOS =====");
            System.out.println("1 - Listar");
            System.out.println("2 - Buscar por código");
            System.out.println("3 - Adicionar");
            System.out.println("4 - Alterar");
            System.out.println("5 - Apagar");
            System.out.println("0 - Voltar");
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> listar();
                case 2 -> buscar();
                case 3 -> adicionar();
                case 4 -> alterar();
                case 5 -> apagar();
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void listar() {
        ArrayList<Servico> servicos = controller.listar();
        if (servicos.isEmpty()) {
            System.out.println("Nenhum serviço cadastrado.");
            return;
        }
        for (Servico servico : servicos) {
            System.out.println(servico);
        }
    }

    private void buscar() {
        int id = lerInteiro("Código do serviço: ");
        Servico servico = controller.buscar(id);
        if (servico == null) {
            System.out.println("Serviço não encontrado.");
        } else {
            System.out.println(servico);
        }
    }

    private void adicionar() {
        int id = lerInteiro("Código do serviço: ");
        if (controller.buscar(id) != null) {
            System.out.println("Já existe um serviço com esse código.");
            return;
        }
        System.out.print("Descrição: ");
        String descricao = teclado.nextLine();
        double valor = lerDouble("Valor: R$ ");

        controller.adicionar(id, descricao, valor);
        System.out.println("Serviço cadastrado com sucesso!");
    }

    private void alterar() {
        int id = lerInteiro("Código do serviço a alterar: ");
        Servico atual = controller.buscar(id);
        if (atual == null) {
            System.out.println("Serviço não encontrado.");
            return;
        }
        System.out.println("Dados atuais: " + atual);
        System.out.print("Nova descrição: ");
        String descricao = teclado.nextLine();
        double valor = lerDouble("Novo valor: R$ ");

        if (controller.alterar(id, descricao, valor)) {
            System.out.println("Serviço alterado com sucesso!");
        } else {
            System.out.println("Não foi possível alterar o serviço.");
        }
    }

    private void apagar() {
        int id = lerInteiro("Código do serviço a apagar: ");
        if (controller.remover(id)) {
            System.out.println("Serviço removido com sucesso!");
        } else {
            System.out.println("Serviço não encontrado.");
        }
    }

    // Lê um inteiro repetindo a pergunta até o usuário digitar um valor válido
    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    // Lê um número decimal (aceita vírgula ou ponto) até ser válido
    private double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Double.parseDouble(teclado.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número, por exemplo 49,90.");
            }
        }
    }
}
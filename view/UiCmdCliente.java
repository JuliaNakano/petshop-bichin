package view;

import controller.ClienteController;
import model.Cliente;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Interface em linha de comando para o cadastro de clientes.
 * Apenas iniciar() é público; as demais operações são privadas.
 */
public class UiCmdCliente {

    // formato exigido para o telefone: (dd) ddddd-dddd
    private static final String FORMATO_TELEFONE = "\\(\\d{2}\\) \\d{5}-\\d{4}";

    private Scanner teclado;
    private ClienteController controller;

    public UiCmdCliente(ClienteController controller) {
        this.teclado = new Scanner(System.in);
        this.controller = controller;
    }

    // mostra o menu de clientes em um laço até o usuário escolher voltar
    public void iniciar() {
        int opcao;
        do {
            System.out.println("\n===== CLIENTES =====");
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
        ArrayList<Cliente> clientes = controller.listar();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    private void buscar() {
        int id = lerInteiro("Código do cliente: ");
        Cliente cliente = controller.buscar(id);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
        } else {
            System.out.println(cliente);
        }
    }

    private void adicionar() {
        // o código é gerado automaticamente pelo sistema
        System.out.print("Nome: ");
        String nome = teclado.nextLine();
        String telefone = lerTelefone();

        Cliente cliente = controller.adicionar(nome, telefone);
        System.out.println("Cliente cadastrado com sucesso! Código gerado: " + cliente.getId());
    }

    private void alterar() {
        int id = lerInteiro("Código do cliente a alterar: ");
        Cliente atual = controller.buscar(id);
        if (atual == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        System.out.println("Dados atuais: " + atual);
        System.out.print("Novo nome: ");
        String nome = teclado.nextLine();
        String telefone = lerTelefone();

        if (controller.alterar(id, nome, telefone)) {
            System.out.println("Cliente alterado com sucesso!");
        } else {
            System.out.println("Não foi possível alterar o cliente.");
        }
    }

    private void apagar() {
        int id = lerInteiro("Código do cliente a apagar: ");
        if (controller.remover(id)) {
            System.out.println("Cliente removido com sucesso!");
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    // lê um inteiro repetindo a pergunta até o usuário digitar um valor válido
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

    // lê o telefone repetindo a pergunta até o formato (dd) ddddd-dddd ser respeitado
    private String lerTelefone() {
        while (true) {
            System.out.print("Telefone ((dd) ddddd-dddd): ");
            String telefone = teclado.nextLine().trim();
            try {
                validarTelefone(telefone);
                return telefone;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // lança exceção se o telefone não estiver no formato correto
    private void validarTelefone(String telefone) {
        if (!telefone.matches(FORMATO_TELEFONE)) {
            throw new IllegalArgumentException("Telefone inválido. Use o formato (dd) ddddd-dddd.");
        }
    }
}
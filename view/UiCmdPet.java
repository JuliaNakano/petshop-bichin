package view;

import controller.ClienteController;
import controller.PetController;
import model.Cliente;
import model.Pet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Interface em linha de comando para o cadastro de pets.
 * Recebe também o controller de Cliente, pois um pet não existe sem cliente:
 * se o cliente informado não existir, ele é cadastrado na hora.
 */
public class UiCmdPet {

    // formato exigido para o telefone: (dd) ddddd-dddd
    private static final String FORMATO_TELEFONE = "\\(\\d{2}\\) \\d{5}-\\d{4}";

    private Scanner teclado;
    private PetController controller;
    private ClienteController controllerCliente;

    // formato brasileiro de data, em modo estrito (rejeita datas como 31/02/2026)
    private DateTimeFormatter formatoData =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public UiCmdPet(PetController controller, ClienteController controllerCliente) {
        this.teclado = new Scanner(System.in);
        this.controller = controller;
        this.controllerCliente = controllerCliente;
    }

    // mostra o menu de pets em um laço até o usuário escolher voltar
    public void iniciar() {
        int opcao;
        do {
            System.out.println("\n===== PETS =====");
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
        ArrayList<Pet> pets = controller.listar();
        if (pets.isEmpty()) {
            System.out.println("Nenhum pet cadastrado.");
            return;
        }
        for (Pet pet : pets) {
            System.out.println(pet);
            System.out.println("-----------------------------");
        }
    }

    private void buscar() {
        int id = lerInteiro("Código do pet: ");
        Pet pet = controller.buscar(id);
        if (pet == null) {
            System.out.println("Pet não encontrado.");
        } else {
            System.out.println(pet);
        }
    }

    private void adicionar() {
        int id = lerInteiro("Código do pet: ");
        if (controller.buscar(id) != null) {
            System.out.println("Já existe um pet com esse código.");
            return;
        }
        System.out.print("Nome: ");
        String nome = teclado.nextLine();
        System.out.print("Espécie: ");
        String especie = teclado.nextLine();
        System.out.print("Raça: ");
        String raca = teclado.nextLine();
        LocalDate nascimento = lerData("Data de nascimento (dd/MM/yyyy): ");

        // o pet precisa de um cliente: usa o existente ou cadastra um novo
        int idCliente = obterCliente();

        try {
            controller.adicionar(id, nome, especie, raca, nascimento, idCliente);
            System.out.println("Pet cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void alterar() {
        int id = lerInteiro("Código do pet a alterar: ");
        Pet atual = controller.buscar(id);
        if (atual == null) {
            System.out.println("Pet não encontrado.");
            return;
        }
        System.out.println("Dados atuais:\n" + atual);
        System.out.print("Novo nome: ");
        String nome = teclado.nextLine();
        System.out.print("Nova espécie: ");
        String especie = teclado.nextLine();
        System.out.print("Nova raça: ");
        String raca = teclado.nextLine();
        LocalDate nascimento = lerData("Nova data de nascimento (dd/MM/yyyy): ");

        // o cliente do pet permanece o mesmo
        int idCliente = atual.getCliente().getId();

        try {
            if (controller.alterar(id, nome, especie, raca, nascimento, idCliente)) {
                System.out.println("Pet alterado com sucesso!");
            } else {
                System.out.println("Não foi possível alterar o pet.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void apagar() {
        int id = lerInteiro("Código do pet a apagar: ");
        if (controller.remover(id)) {
            System.out.println("Pet removido com sucesso!");
        } else {
            System.out.println("Pet não encontrado.");
        }
    }

    // pede o código do cliente; se ele não existir, cadastra o cliente neste momento
    // (o código do novo cliente é gerado automaticamente)
    private int obterCliente() {
        int idCliente = lerInteiro("Código do cliente (dono) - digite 0 se for um cliente novo: ");

        Cliente existente = controllerCliente.buscar(idCliente);
        if (existente != null) {
            return existente.getId();
        }

        System.out.println("Cliente não encontrado. Vamos cadastrá-lo agora.");
        System.out.print("Nome do cliente: ");
        String nomeCliente = teclado.nextLine();
        String telefone = lerTelefone();

        Cliente novo = controllerCliente.adicionar(nomeCliente, telefone);
        System.out.println("Cliente cadastrado com sucesso! Código gerado: " + novo.getId());
        return novo.getId();
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

    // lê uma data no formato dd/MM/yyyy até o usuário informar uma data válida
    private LocalDate lerData(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return LocalDate.parse(teclado.nextLine().trim(), formatoData);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd/MM/yyyy.");
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
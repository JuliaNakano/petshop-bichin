package view;

import controller.ClienteController;
import controller.PetController;
import controller.ServicoController;
import controller.ServicoPrestadoController;
import model.Cliente;
import model.Pet;
import model.Servico;
import model.ServicoPrestado;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Interface em linha de comando para o cadastro de serviços prestados.
 * Apenas iniciar() é público; as demais operações são privadas.
 */
public class UiCmdServicoPrestado {

    private Scanner teclado;
    private ServicoPrestadoController controller;
    private ClienteController controllerCliente;
    private ServicoController controllerServico;
    private PetController controllerPet;

    // formato brasileiro de data, em modo estrito (rejeita datas como 31/02/2026)
    private DateTimeFormatter formatoData =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public UiCmdServicoPrestado(ServicoPrestadoController controller,
                                ClienteController controllerCliente,
                                ServicoController controllerServico,
                                PetController controllerPet) {
        this.teclado = new Scanner(System.in);
        this.controller = controller;
        this.controllerCliente = controllerCliente;
        this.controllerServico = controllerServico;
        this.controllerPet = controllerPet;
    }

    // mostra o menu em um laço até o usuário escolher voltar
    public void iniciar() {
        int opcao;
        do {
            System.out.println("\n===== SERVIÇOS PRESTADOS =====");
            System.out.println("1 - Listar");
            System.out.println("2 - Buscar por código");
            System.out.println("3 - Adicionar");
            System.out.println("4 - Apagar");
            System.out.println("5 - Serviços por período");
            System.out.println("6 - Animais do dono");
            System.out.println("7 - Maior valor cobrado de um cliente");
            System.out.println("8 - Serviços de um cliente");
            System.out.println("0 - Voltar");
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> listar();
                case 2 -> buscar();
                case 3 -> adicionar();
                case 4 -> apagar();
                case 5 -> servicosPorPeriodo();
                case 6 -> animaisDoDono();
                case 7 -> maiorValor();
                case 8 -> servicosDoCliente();
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void listar() {
        mostrarServicos(controller.listar());
    }

    private void buscar() {
        int id = lerInteiro("Código do serviço prestado: ");
        ServicoPrestado sp = controller.buscar(id);
        if (sp == null) {
            System.out.println("Serviço prestado não encontrado.");
        } else {
            System.out.println(sp);
        }
    }

    private void adicionar() {
        int id = lerInteiro("Código do serviço prestado: ");
        if (controller.buscar(id) != null) {
            System.out.println("Já existe um serviço prestado com esse código.");
            return;
        }

        int idPet = lerInteiro("Código do pet: ");
        Pet pet = controllerPet.buscar(idPet);
        if (pet == null) {
            System.out.println("Pet não encontrado.");
            return;
        }

        int idServico = lerInteiro("Código do serviço: ");
        Servico servico = controllerServico.buscar(idServico);
        if (servico == null) {
            System.out.println("Serviço não encontrado.");
            return;
        }

        // mostra o serviço escolhido e o valor antes de pedir o valor efetivamente cobrado
        System.out.println("Serviço escolhido: " + servico.getDescricao());
        System.out.println(String.format("Valor do serviço: R$ %.2f", servico.getValor()));
        double valorCobrado = lerDouble("Valor cobrado: R$ ");
        LocalDate data = lerData("Data do serviço (dd/MM/yyyy): ");

        try {
            controller.adicionar(id, idPet, idServico, valorCobrado, data);
            System.out.println("Serviço prestado cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void apagar() {
        int id = lerInteiro("Código do serviço prestado a apagar: ");
        if (controller.removerServicoPrestado(id)) {
            System.out.println("Serviço prestado removido com sucesso!");
        } else {
            System.out.println("Serviço prestado não encontrado.");
        }
    }

    private void servicosPorPeriodo() {
        LocalDate inicio = lerData("Data inicial (dd/MM/yyyy): ");
        LocalDate fim = lerData("Data final (dd/MM/yyyy): ");
        if (fim.isBefore(inicio)) {
            System.out.println("A data final não pode ser anterior à data inicial.");
            return;
        }
        mostrarServicos(controller.buscarPorPeriodo(inicio, fim));
    }

    private void animaisDoDono() {
        System.out.print("Nome do dono: ");
        String nome = teclado.nextLine();
        ArrayList<Pet> bichos = controller.buscarBichosPorNomeDono(nome);

        if (bichos.isEmpty()) {
            System.out.println("Nenhum animal atendido encontrado para esse dono.");
            return;
        }
        for (Pet pet : bichos) {
            System.out.println("Pet " + pet.getId() + " - " + pet.getNome());
        }
    }

    private void maiorValor() {
        Cliente cliente = lerCliente();
        if (cliente == null) {
            return;
        }
        double maior = controller.maiorValorPorCliente(cliente);
        System.out.println(String.format("Maior valor cobrado de %s: R$ %.2f", cliente.getNome(), maior));
    }

    private void servicosDoCliente() {
        Cliente cliente = lerCliente();
        if (cliente == null) {
            return;
        }
        mostrarServicos(controller.buscarPorCliente(cliente));
    }

    // pede o código do cliente e o busca; retorna null se não existir
    private Cliente lerCliente() {
        int id = lerInteiro("Código do cliente: ");
        Cliente cliente = controllerCliente.buscar(id);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
        }
        return cliente;
    }

    private void mostrarServicos(ArrayList<ServicoPrestado> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhum serviço prestado encontrado.");
            return;
        }
        for (ServicoPrestado sp : lista) {
            System.out.println(sp);
            System.out.println("-----------------------------");
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

    // lê um número decimal (aceita vírgula ou ponto) até ser válido
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
}

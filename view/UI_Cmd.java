package view;

import java.util.Scanner;

/**
 * Menu principal da aplicação em linha de comando.
 * Recebe as telas de cada opção pelo construtor (injeção de dependência)
 * e chama o método iniciar() da tela escolhida.
 */
public class UI_Cmd {

    private Scanner teclado;

    private UiCmdCliente uiCmdCliente;
    private UiCmdPet uiCmdPet;
    private UiCmdServico uiCmdServico;
    private UiCmdServicoPrestado menuServicoPrestado;

    public UI_Cmd(UiCmdCliente uiCmdCliente,
                  UiCmdPet uiCmdPet,
                  UiCmdServico uiCmdServico,
                  UiCmdServicoPrestado menuServicoPrestado) {

        this.teclado = new Scanner(System.in);

        this.uiCmdCliente = uiCmdCliente;
        this.uiCmdPet = uiCmdPet;
        this.uiCmdServico = uiCmdServico;
        this.menuServicoPrestado = menuServicoPrestado;
    }

    // mostra o menu de opções em um laço de repetição,
    // lê a opção desejada pelo usuário e chama o método iniciar() da tela escolhida
    public void iniciar() {
        int opcao;
        do {
            System.out.println("\n==============================");
            System.out.println("          PET SHOP");
            System.out.println("==============================");
            System.out.println("1 - Clientes");
            System.out.println("2 - Pets");
            System.out.println("3 - Serviços");
            System.out.println("4 - Serviços Prestados");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
                opcao = -1;
                continue;
            }

            switch (opcao) {
                case 1 -> uiCmdCliente.iniciar();
                case 2 -> uiCmdPet.iniciar();
                case 3 -> uiCmdServico.iniciar();
                case 4 -> menuServicoPrestado.iniciar();
                case 0 -> System.out.println("Encerrando o sistema. Até logo!");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}

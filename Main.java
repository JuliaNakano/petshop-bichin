import controller.ClienteController;
import controller.PetController;
import controller.ServicoController;
import controller.ServicoPrestadoController;
import repository.ClienteRepository;
import repository.PetRepository;
import repository.ServicoPrestadoRepository;
import repository.ServicoRepository;
import view.UI_Cmd;
import view.UiCmdCliente;
import view.UiCmdPet;
import view.UiCmdServico;
import view.UiCmdServicoPrestado;

public class Main {

    public static void main(String[] args) {

        /*
         * ========================================
         * REPOSITORIES (camada de persistência)
         * Armazenam os dados em ArrayList e serão injetados nos controllers.
         * ========================================
         */
        ClienteRepository clienteRepository = new ClienteRepository();
        PetRepository petRepository = new PetRepository();
        ServicoRepository servicoRepository = new ServicoRepository();
        ServicoPrestadoRepository servicoPrestadoRepository = new ServicoPrestadoRepository();

        /*
         * ========================================
         * CARREGAMENTO DOS DADOS INICIAIS (arquivos texto)
         * A ordem importa: pets precisam dos clientes, e serviços prestados
         * precisam dos pets e dos serviços já carregados.
         * ========================================
         */
        clienteRepository.carregarDados("dados/clientes.txt");
        petRepository.carregarDados("dados/pets.txt", clienteRepository);
        servicoRepository.carregarDados("dados/servicos.txt");
        servicoPrestadoRepository.carregarDados(
                "dados/servicos_prestados.txt", petRepository, servicoRepository);

        /*
         * ========================================
         * CONTROLLERS (intermediam a visão e os repositories)
         * ========================================
         */
        ClienteController clienteController = new ClienteController(clienteRepository);
        PetController petController = new PetController(petRepository, clienteRepository);
        ServicoController servicoController = new ServicoController(servicoRepository);
        ServicoPrestadoController servicoPrestadoController = new ServicoPrestadoController(
                servicoPrestadoRepository, petRepository, servicoRepository);

        /*
         * ========================================
         * TELAS (uma classe de interface para cada opção do menu)
         * ========================================
         */
        UiCmdCliente uiCmdCliente = new UiCmdCliente(clienteController);
        UiCmdPet uiCmdPet = new UiCmdPet(petController, clienteController);
        UiCmdServico uiCmdServico = new UiCmdServico(servicoController);
        UiCmdServicoPrestado menuServicoPrestado = new UiCmdServicoPrestado(
                servicoPrestadoController, clienteController, servicoController, petController);

        /*
         * ========================================
         * MENU PRINCIPAL
         * ========================================
         */
        UI_Cmd uiCmd = new UI_Cmd(uiCmdCliente, uiCmdPet, uiCmdServico, menuServicoPrestado);
        uiCmd.iniciar();
    }
}
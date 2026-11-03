import model.Cliente;
import model.Pet;
import repository.ClienteRepository;

void main() {



    /*/Pet pet = new Pet(
            1,
            "Rex",
            "cachorro",
            "Labrador",
            cliente1,
            LocalDate.of(2022, 5, 10)
    );/*/
    ClienteRepository repositorio = new ClienteRepository();
    Cliente cliente1 = new Cliente(2, "Julia", "31999999999");
    Cliente cliente2 = new Cliente(1, "Maria", "31999999999");

    repositorio.adicionarCliente(cliente1);
    repositorio.adicionarCliente(cliente2);

    //procurando pelo id
    //Cliente encontrado = repositorio.buscarClientePorId(25);
    repositorio.removerCliente(2);
    repositorio.listarClientes();


    //repositorio.listarClientes();

    //System.out.println(pet);
}
package repository;

import model.Cliente;
import java.util.ArrayList;

public class ClienteRepository {

    //armazenar objetos clientes
    private ArrayList<Cliente> clientes = new ArrayList<>();

    public void adicionarCliente(Cliente cliente) {
            clientes.add(cliente);
    }
    //listando os clientes
    public void listarClientes() {
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }
    //busca pelo o id do cliente
    public Cliente buscarClientePorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id){
                System.out.println("O cliente existe e é:"+cliente);
                return cliente;
            }
        }
        System.out.println("O cliente solicitado pelo id não existe");
        return null;
    }
}
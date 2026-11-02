package model;

public class Cliente {

    //atributos
    private int id;
    private String nome;
    private String telefone;

    //construtor
    public Cliente(){
    }

    public Cliente(int id, String nome, String telefone){
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "Cliente: " + id + " - " + nome + " - " + telefone;
    }


    //verifica se os id são iguais
    @Override
    public boolean equals(Object obj) {
        //verifica se os dois ocupam a mesma memoria
        if (this == obj) {
            System.out.println("Os dois obj ocupam a mesma memoria!");
            return true;
        }
        //verifica se o objeto recebido não é da classe Cliente
        if (!(obj instanceof Cliente)) {
            System.out.println("Os dois obj a mesma classe!");
            return false;
        }

        Cliente outroCliente = (Cliente) obj;
        System.out.println("");
        return this.id == outroCliente.getId();
    }

    @Override
    //aqui nao entendi muito bem, mesmo pesquisando
    public int hashCode() {
        return id;
    }

    //metodos get
    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public String getTelefone() {
        return telefone;
    }

    //metodos set
    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}

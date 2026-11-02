package model;

public class Cliente {

    //atributos
    private int id;
    private String nome;
    private String telefone;

    //construtor
    public Cliente(int id, String nome, String telefone){
        this.id = id;
        this.telefone = telefone;
        this.nome = nome;
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
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}

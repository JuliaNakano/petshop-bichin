package model;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Pet {
    //atributos
    private int id;
    private String nome;
    private String especie;
    private String raca;
    private Cliente cliente; //talvez aqui seja chave estrangeira sabe?
    private LocalDate nascimento;

    //construtor
    public Pet(){

    }

    public Pet(int id, String nome, String especie, String raca, Cliente cliente, LocalDate nascimento){
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.cliente = cliente;
        this.nascimento = nascimento;
    }
    // metodos get e set

    public LocalDate getNascimento() {
        return nascimento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getEspecie() {
        return especie;
    }

    public int getIdade(){
        //ano atual menos data de nascimento
        if (nascimento == null) {
            return 0;
        }

        return Period.between(nascimento, LocalDate.now()).getYears();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    @Override
    public String toString() {
        String nomeTutor;
        //verifica se o pet possui um cliente cadastrado
        if (cliente != null) {
            nomeTutor = cliente.getNome();
        } else {
            nomeTutor = "Não informado";
        }
        // para formatar a data
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //retorna as informações do pet
        return "Pet: " + id
                + "\n Nome:" + nome
                + "\n Especie:" + especie
                + "\n Raca:" + raca
                + "\n Nascimento:" + nascimento.format(formato)
                + "\n Tutor: " + nomeTutor;
    }



    @Override
    public boolean equals(Object obj) {
        //verifica se é o mesmo objeto
        if (this == obj) {
            return true;
        }

        //verifica se o objeto recebido é um Pet
        if (!(obj instanceof Pet)) {
            return false;
        }

        //converte o objeto recebido para Pet
        Pet outroPet = (Pet) obj;

        //compara os IDs
        return this.id == outroPet.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
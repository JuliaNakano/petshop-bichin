import model.Cliente;
import model.Pet;

void main() {

    Cliente cliente1 = new Cliente(2, "Julia", "31999999999");

    Pet pet = new Pet(
            1,
            "Rex",
            "cachorro",
            "Labrador",
            cliente1,
            LocalDate.of(2022, 5, 10)
    );

    System.out.println(pet);
}
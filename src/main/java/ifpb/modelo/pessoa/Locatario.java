package ifpb.modelo.pessoa;

import ifpb.enumeradores.Sexo;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor()

@DiscriminatorValue("LOCATARIO")
@Entity
public class Locatario extends Pessoa {
    public Locatario(String nome, Sexo sexo, String cpf, String email, LocalDate dataDeNascimento, String telefone) {
        super(nome, sexo, cpf, email, dataDeNascimento, telefone);
    }
}

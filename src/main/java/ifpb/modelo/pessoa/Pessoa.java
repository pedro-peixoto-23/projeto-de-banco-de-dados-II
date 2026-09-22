package ifpb.modelo.pessoa;

import ifpb.enumeradores.Sexo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Representa os dados comuns das pessoas cadastradas no sistema.
 *
 * Serve como classe base para locatários e espectadores.
 * A persistência utiliza a estratégia SINGLE_TABLE, com um
 * discriminador responsável por identificar cada tipo de pessoa.
 */

@Getter
@NoArgsConstructor()

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_pessoa")

@Entity
@Table(name = "tb_pessoa")
public abstract class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sexo sexo;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate dataDeNascimento;

    @Column(nullable = false)
    private String telefone;

    public Pessoa(String nome, Sexo sexo, String cpf, String email, LocalDate dataDeNascimento, String telefone) {
        this.nome = nome;
        this.sexo = sexo;
        this.cpf = cpf;
        this.email = email;
        this.dataDeNascimento = dataDeNascimento;
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Pessoa)) {
            return false;
        }

        Pessoa pessoa = (Pessoa) object;

        return (cpf != null) && (cpf.equals(pessoa.getCpf()));
    }

    @Override
    public int hashCode() {
        if (cpf != null) return cpf.hashCode();
        return 0;
    }
}
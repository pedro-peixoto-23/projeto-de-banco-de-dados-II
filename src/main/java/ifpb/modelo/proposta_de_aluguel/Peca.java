package ifpb.modelo.proposta_de_aluguel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

@Embeddable
public class Peca {
    @Column(name = "nome_peca", nullable = false)
    private String nome;

    public Peca(String nome) {
        this.nome = nome;
    }
}
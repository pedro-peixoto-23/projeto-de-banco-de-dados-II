package ifpb.modelo.ingresso;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Representa um ingresso individual gerado a partir
 * de uma venda de ingressos.
 *
 * Cada ingresso possui identidade própria e permanece associado
 * à venda responsável por sua criação.
 */

@NoArgsConstructor()
@Getter

@Table(name = "tb_ingresso")
@Entity
public class Ingresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private VendaDeIngresso vendaDeIngresso;

    public Ingresso(VendaDeIngresso vendaDeIngresso) {
        this.vendaDeIngresso = vendaDeIngresso;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Ingresso)) {
            return false;
        }

        Ingresso ingresso = (Ingresso) object;

        return (id != null) && (id.equals(ingresso.getId()));
    }

    @Override
    public int hashCode() {
        if (id != null) return id.hashCode();
        return 0;
    }
}

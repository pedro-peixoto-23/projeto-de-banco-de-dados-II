package ifpb.modelo.proposta_de_aluguel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor()

@Embeddable
public class ControleValorDiarioAluguel {
    @Column(nullable = false)
    private LocalDate dia;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
}
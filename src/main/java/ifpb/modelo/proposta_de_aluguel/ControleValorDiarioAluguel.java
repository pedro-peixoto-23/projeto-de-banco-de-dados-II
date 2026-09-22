package ifpb.modelo.proposta_de_aluguel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa o valor de aluguel calculado para um dia específico
 * de um período de exibição.
 *
 * É armazenado como elemento da coleção de valores diários
 * de PeriodoExibicaoPeca e não possui identidade própria.
 */

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
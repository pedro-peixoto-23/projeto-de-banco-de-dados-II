package ifpb.modelo.proposta_de_aluguel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Reúne os resultados financeiros de uma proposta de aluguel.
 *
 * Contém o total arrecadado com a venda de ingressos,
 * o valor do aluguel e o valor líquido resultante
 * da diferença entre arrecadação e aluguel.
 */

@AllArgsConstructor
@Getter
public class ControleFinanceiroProposta {
    private BigDecimal totalArrecadado;
    private BigDecimal valorAluguel;
    private BigDecimal valorLiquido;
}
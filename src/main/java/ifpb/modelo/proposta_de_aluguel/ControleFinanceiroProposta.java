package ifpb.modelo.proposta_de_aluguel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ControleFinanceiroProposta {
    private BigDecimal totalArrecadado;
    private BigDecimal valorAluguel;
    private BigDecimal valorLiquido;
}
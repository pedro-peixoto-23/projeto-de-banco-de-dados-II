package ifpb.modelo.ingresso;

import ifpb.modelo.pessoa.Espectador;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representa o controle de presença de um espectador
 * em uma determinada exibição.
 *
 * Mantém o espectador e a quantidade total de ingressos
 * associados a ele para a geração da lista de presença.
 */

@Getter
@AllArgsConstructor
public class ControlePresenca {
    private Espectador espectador;
    private int quantidadeIngressos;

    /**
     * Acrescenta uma quantidade de ingressos ao total
     * registrado para o espectador.
     *
     * @param quantidade quantidade de ingressos que será adicionada.
     */
    public void adicionarIngressos(int quantidade) {
        quantidadeIngressos += quantidade;
    }
}

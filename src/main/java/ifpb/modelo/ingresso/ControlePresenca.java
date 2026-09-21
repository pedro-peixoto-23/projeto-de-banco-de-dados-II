package ifpb.modelo.ingresso;

import ifpb.modelo.pessoa.Espectador;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ControlePresenca {
    private Espectador espectador;
    private int quantidadeIngressos;

    public void adicionarIngressos(int quantidade) {
        quantidadeIngressos += quantidade;
    }
}

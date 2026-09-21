package ifpb.telas.componente_personalizado;

import java.awt.Color;

public class BotaoTelaInicial extends BotaoPadrao {

    public BotaoTelaInicial(String texto, Color corFundo, Color corLetra, int posicaoX, int posicaoY, int comprimento) {
        super(texto, corFundo, corLetra, posicaoX, posicaoY, comprimento);
        setBounds(posicaoX, posicaoY, comprimento, 162);
    }
    
}

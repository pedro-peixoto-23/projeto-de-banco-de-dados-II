package ifpb.telas.componente_personalizado;

import ifpb.telas.configuracao.FontesPadroes;

import java.awt.*;


public class LabelTitulo extends LabelPadrao {
    public LabelTitulo(Color cor, String texto, int posicaoX, int posicaoY, int comprimento, int altura) {
        super(texto, posicaoX, posicaoY, comprimento, altura);
        this.setFont(FontesPadroes.labelTitulo);
        this.setForeground(cor);
        this.setHorizontalAlignment(LabelPadrao.CENTER);
    }
}

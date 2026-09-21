package ifpb.telas.componente_personalizado;

import ifpb.telas.configuracao.FontesPadroes;

import java.awt.*;


public class LabelSubTitulo extends LabelPadrao {
    public LabelSubTitulo(Color cor, String texto, int posicaoX, int posicaoY, int comprimento, int altura) {
        super(texto, posicaoX, posicaoY, comprimento, altura);
        setFont(FontesPadroes.labelSubTitulo);
        setForeground(cor);
        setHorizontalAlignment(LabelPadrao.LEFT);
    }
}


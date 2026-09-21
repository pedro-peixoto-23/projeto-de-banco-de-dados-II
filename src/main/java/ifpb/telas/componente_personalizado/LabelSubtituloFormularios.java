package ifpb.telas.componente_personalizado;

import ifpb.telas.configuracao.FontesPadroes;

import java.awt.*;


public class LabelSubtituloFormularios extends LabelPadrao {
	public LabelSubtituloFormularios(Color cor, String texto, int posicaoX, int posicaoY, int comprimento, int altura) {
        super(texto, posicaoX, posicaoY, comprimento, altura);
        setFont(FontesPadroes.labelSubTitulosFormulario);
        setForeground(cor);
        setHorizontalAlignment(LabelPadrao.LEFT);
    }
}

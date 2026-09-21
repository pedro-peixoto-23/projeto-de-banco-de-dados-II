package ifpb.telas.componente_personalizado;

import ifpb.telas.configuracao.FontesPadroes;
import ifpb.telas.configuracao.PaletaDeCores;

import javax.swing.*;


public class LabelPadrao extends JLabel {
    public LabelPadrao(String texto, int posicaoX, int posicaoY, int comprimento, int altura) {
        this.setText(texto);
        this.setFont(FontesPadroes.label);
        this.setBounds(posicaoX, posicaoY, comprimento, altura);
        this.setForeground(PaletaDeCores.preto);
    }
}

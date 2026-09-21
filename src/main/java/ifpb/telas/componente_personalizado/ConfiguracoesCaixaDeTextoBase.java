package ifpb.telas.componente_personalizado;

import ifpb.telas.configuracao.FontesPadroes;
import ifpb.telas.configuracao.PaletaDeCores;

import javax.swing.*;


public class ConfiguracoesCaixaDeTextoBase {
    public static void aplicarFormatacoes(JComponent caixa, int posicaoX, int posicaoY, int comprimento, int altura) {
    	caixa.setFont(FontesPadroes.caixaDeTexto);
    	caixa.setBounds(posicaoX, posicaoY, comprimento, altura);
    	caixa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PaletaDeCores.preto, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }
}

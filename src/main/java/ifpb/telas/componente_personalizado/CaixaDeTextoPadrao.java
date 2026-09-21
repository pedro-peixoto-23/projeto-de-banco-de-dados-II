package ifpb.telas.componente_personalizado;
import ifpb.telas.configuracao.ValoresPadroes;

import javax.swing.JTextField;



public class CaixaDeTextoPadrao extends JTextField {
    public CaixaDeTextoPadrao(int posicaoX, int posicaoY, int comprimento) {
        ConfiguracoesCaixaDeTextoBase.aplicarFormatacoes(this, posicaoX, posicaoY, comprimento, ValoresPadroes.alturaCaixasDeTexto);
    }
}

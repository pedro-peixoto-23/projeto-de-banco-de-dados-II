package ifpb.telas.componente_personalizado;
import ifpb.telas.configuracao.ValoresPadroes;

import javax.swing.JPasswordField;


public class CaixaDeTextoSenhas extends JPasswordField {
    public CaixaDeTextoSenhas(int posicaoX, int posicaoY, int comprimento) {
        ConfiguracoesCaixaDeTextoBase.aplicarFormatacoes(this, posicaoX, posicaoY, comprimento, ValoresPadroes.alturaCaixasDeTexto);
    }
}

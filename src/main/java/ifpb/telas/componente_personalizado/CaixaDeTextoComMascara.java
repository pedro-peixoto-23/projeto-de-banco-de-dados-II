package ifpb.telas.componente_personalizado;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

public class CaixaDeTextoComMascara extends JFormattedTextField {
	public CaixaDeTextoComMascara(MaskFormatter mascara, int posicaoX, int posicaoY, int comprimento, int altura) {
		super(mascara);
		ConfiguracoesCaixaDeTextoBase.aplicarFormatacoes(this, posicaoX, posicaoY, comprimento, altura);
	}	
}

package ifpb.telas.componente_personalizado;

import ifpb.telas.configuracao.FontesPadroes;

import javax.swing.*;


public class ComboBoxPadrao<T> extends JComboBox<T> {
	public ComboBoxPadrao(T[] lista, int posicaoX, int posicaoY, int comprimento, int altura) {
		super(lista);
		this.setBounds(posicaoX, posicaoY, comprimento, altura);
		this.setFont(FontesPadroes.caixaDeTexto);
	}
}

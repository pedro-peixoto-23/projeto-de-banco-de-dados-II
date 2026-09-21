package ifpb.telas.componente_personalizado;
import ifpb.telas.configuracao.FontesPadroes;
import ifpb.telas.configuracao.ValoresPadroes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public class BotaoPadrao extends JButton {
    
    public BotaoPadrao(String texto, Color corFundo, Color corLetra, int posicaoX, int posicaoY, int comprimento) {
        setText(texto);
        setFont(FontesPadroes.botao);
        setBackground(corFundo);
        setForeground(corLetra);
        definirFormatacoesGerais(posicaoX, posicaoY, comprimento, ValoresPadroes.alturaBotoes);
    }
    
    public BotaoPadrao(ImageIcon imagem, int posicaoX, int posicaoY, int comprimento, int altura) {
    	setIcon(imagem);
    	definirFormatacoesGerais(posicaoX, posicaoY, comprimento, altura);
    }
    
    public void definirFormatacoesGerais(int posicaoX, int posicaoY, int comprimento, int altura) {
    	setBounds(posicaoX, posicaoY, comprimento, altura);
    	setBorderPainted(false);
		setFocusPainted(false);
		setOpaque(true);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void definirFormatacoesSemAparenciaDeBotao() {
        setOpaque(false);
        setContentAreaFilled(false);
        setMargin(new Insets(0, 0, 0, 0));
    }
}

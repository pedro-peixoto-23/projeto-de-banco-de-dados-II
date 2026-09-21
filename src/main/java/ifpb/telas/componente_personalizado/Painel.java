package ifpb.telas.componente_personalizado;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class Painel extends JPanel {
    public Painel(Color cor, int comprimento, int altura) {
        this.setPreferredSize(new Dimension(comprimento, altura));
        this.aplicarFormatacaoGeral(cor);
    }
    
    public Painel(Color cor, int posicaoX, int posicaoY, int comprimento, int altura) {
    	this.setBounds(posicaoX, posicaoY, comprimento, altura);
        this.aplicarFormatacaoGeral(cor);
    }
    
    public void aplicarFormatacaoGeral(Color cor) {
    	this.setBackground(cor);
        this.setLayout(null);
    }
}

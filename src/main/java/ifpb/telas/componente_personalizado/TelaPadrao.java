package ifpb.telas.componente_personalizado;

import javax.swing.*;
import java.awt.*;

public abstract class TelaPadrao extends JFrame {
    private int xTamanho;
    private int yTamanho;

    public TelaPadrao(String titulo, Color corDeFundo, int xTamanho, int yTamanho) {  
        this.xTamanho = xTamanho;
        this.yTamanho = yTamanho;
        
        this.setTitle(titulo);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.getContentPane().setBackground(corDeFundo);

        this.desenhar();

        pack();
                
        Insets insets = this.getInsets();
        this.setMinimumSize(new Dimension(xTamanho + insets.left + insets.right, yTamanho + insets.top  + insets.bottom));

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public int getXTamanho() {
        return xTamanho;
    }

    public int getYTamanho() {
        return yTamanho;
    }

    public abstract void desenhar();
}

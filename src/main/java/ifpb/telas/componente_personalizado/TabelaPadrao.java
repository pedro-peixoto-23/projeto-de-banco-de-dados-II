package ifpb.telas.componente_personalizado;

import ifpb.telas.configuracao.FontesPadroes;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class TabelaPadrao extends JTable {
    public TabelaPadrao(DefaultTableModel modeloDeDados) {
        super(modeloDeDados);
        getTableHeader().setPreferredSize(new Dimension(0, ValoresPadroes.alturaLinhaTabela));		
		getTableHeader().setFont(FontesPadroes.tituloTabela);
		getTableHeader().setForeground(PaletaDeCores.preto);
		setRowHeight(ValoresPadroes.alturaLinhaTabela);
		setFont(FontesPadroes.corpoTabela);
		setForeground(PaletaDeCores.preto);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
}

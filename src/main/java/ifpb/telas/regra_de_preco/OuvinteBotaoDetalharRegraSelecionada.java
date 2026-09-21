package ifpb.telas.regra_de_preco;

import ifpb.modelo.regra_de_preco.RegraDePreco;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

public class OuvinteBotaoDetalharRegraSelecionada implements ActionListener {
    private final TelaRegrasDePreco tela;

    public OuvinteBotaoDetalharRegraSelecionada(TelaRegrasDePreco tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoDetalharPressionado) {
        tela.zerarDetalhamento();

        JTable tabelaRegraDePreco = tela.getTabelaRegraDePreco();

        int linhaSelecionada = tabelaRegraDePreco.getSelectedRow();

        if (linhaSelecionada < 0) {
            JOptionPane.showMessageDialog(null, "Nenhuma linha foi selecionada!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        RegraDePreco regraDePrecoSelecionada = (RegraDePreco) tabelaRegraDePreco.getValueAt(linhaSelecionada, 1);

        tela.setRegraDePrecoSelecionada(regraDePrecoSelecionada);

        tela.getCaixaTextoValor().setText(String.valueOf(regraDePrecoSelecionada.getValorPorHora()));

        if (regraDePrecoSelecionada.getAno() != null) {
            tela.getCaixaTextoAno().setText(String.valueOf(regraDePrecoSelecionada.getAno()));
        } else {
            tela.getCaixaTextoAno().setText("");
        }

        if (regraDePrecoSelecionada.getMes() != null) {
            tela.getEscolhaMes().setSelectedIndex(regraDePrecoSelecionada.getMes().ordinal());
        } else {
            tela.getEscolhaMes().setSelectedIndex(0);
        }

        if (regraDePrecoSelecionada.getDiaDaSemana() != null) {
            tela.getEscolhaDia().setSelectedIndex(regraDePrecoSelecionada.getDiaDaSemana().ordinal());
        } else {
            tela.getEscolhaDia().setSelectedIndex(0);
        }

        if (regraDePrecoSelecionada.getTurno() != null) {
            tela.getEscolhaTurno().setSelectedIndex(regraDePrecoSelecionada.getTurno().ordinal());
        } else {
            tela.getEscolhaTurno().setSelectedIndex(0);
        }

        if (regraDePrecoSelecionada.getIntervaloDeHorario() != null) {
            tela.getCaixaTextoHorarioInicio().setText(regraDePrecoSelecionada.getIntervaloDeHorario().getHorarioInicio().toString());

            tela.getCaixaTextoHorarioFim().setText(regraDePrecoSelecionada.getIntervaloDeHorario().getHorarioFim().toString());

        } else {
            tela.getCaixaTextoHorarioInicio().setText("");
            tela.getCaixaTextoHorarioFim().setText("");
        }
    }
}
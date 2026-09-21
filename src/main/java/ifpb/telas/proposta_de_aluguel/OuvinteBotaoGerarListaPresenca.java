package ifpb.telas.proposta_de_aluguel;

import ifpb.modelo.ingresso.ControlePresenca;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.relatorios_ou_contratos.csv.GeradorPlanilhaPresenca;
import ifpb.telas.Sessao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class OuvinteBotaoGerarListaPresenca implements ActionListener {
    private final TelaListaPresenca tela;

    public OuvinteBotaoGerarListaPresenca(TelaListaPresenca tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoGerarListaPresencaPressionado) {
        PropostaDeAluguel propostaDeAluguel = Sessao.getPropostaDeAluguelAtualParaDetalhamento();

        try {
            if (tela.getCheckboxTodasDatas().isSelected()) {
                GeradorPlanilhaPresenca.gerarPlanilhaGeral(propostaDeAluguel);
            } else {
                LocalDate dataDaPeca = (LocalDate) tela.getEscolhaDataPeca().getSelectedItem();

                if (dataDaPeca == null) {
                    JOptionPane.showMessageDialog(null, "Selecione uma data da peça!", "Atenção", JOptionPane.WARNING_MESSAGE);

                    return;
                }

                List<ControlePresenca> listaPresenca = Sessao.getDaoVendaDeIngresso().gerarListaPresencaPorDataParaProposta(propostaDeAluguel, dataDaPeca);

                GeradorPlanilhaPresenca.gerarPlanilhaPorData(propostaDeAluguel, dataDaPeca, listaPresenca);
            }
            JOptionPane.showMessageDialog(null, "Planilha gerada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Não foi possível gerar a planilha!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

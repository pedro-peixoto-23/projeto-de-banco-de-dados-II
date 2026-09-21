package ifpb.telas.proposta_de_aluguel;

import ifpb.telas.Sessao;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

@Getter
public class TelaListaPresenca extends TelaPadrao {
    private JCheckBox checkboxTodasDatas;
    private ComboBoxPadrao<LocalDate> escolhaDataPeca;

    public TelaListaPresenca() {
        super("Lista de presença", PaletaDeCores.branco, 380, 369);
    }

    public void desenhar() {
        Painel painelListaPresenca = new Painel(PaletaDeCores.branco, getXTamanho(), getYTamanho());

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Lista de presença", 35, 35, 310, ValoresPadroes.alturaLabelTitulo);
        painelListaPresenca.add(labelTitulo);

        LabelPadrao labelTodasDatas = new LabelPadrao("Selecionar todo o período ativo", 35, 120, 310, ValoresPadroes.alturaLabel);
        painelListaPresenca.add(labelTodasDatas);

        checkboxTodasDatas = new JCheckBox();
        checkboxTodasDatas.setBounds(35, 146, 25, 32);
        checkboxTodasDatas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent checkboxSelecionado) {
                escolhaDataPeca.setEnabled(!checkboxTodasDatas.isSelected());
            }
        });
        painelListaPresenca.add(checkboxTodasDatas);

        LabelPadrao labelDataEspecifica = new LabelPadrao("Data da peça", 35, 201, 310, ValoresPadroes.alturaLabel);
        painelListaPresenca.add(labelDataEspecifica);

        escolhaDataPeca = new ComboBoxPadrao<>(Sessao.getPropostaDeAluguelAtualParaDetalhamento().gerarDatasExibicao().toArray(new LocalDate[0]), 35, 227, 310, ValoresPadroes.alturaCaixasDeTexto);
        escolhaDataPeca.setSelectedIndex(-1);
        painelListaPresenca.add(escolhaDataPeca);

        BotaoPadrao botaoGerarPlanilha = new BotaoPadrao("Gerar Planilha", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 302, 186);
        botaoGerarPlanilha.addActionListener(
                new OuvinteBotaoGerarListaPresenca(this)
        );
        painelListaPresenca.add(botaoGerarPlanilha);

        BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 232, 302, 113);
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoCancelarPressionado) {
                dispose();
            }
        });
        painelListaPresenca.add(botaoCancelar);

        add(painelListaPresenca);
    }
}

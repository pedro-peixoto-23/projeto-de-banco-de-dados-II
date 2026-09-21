package ifpb.telas.regra_de_preco;

import ifpb.enumeradores.DiaDaSemana;
import ifpb.enumeradores.Mes;
import ifpb.enumeradores.Turno;
import ifpb.modelo.regra_de_preco.RegraDePreco;
import ifpb.telas.Sessao;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import ifpb.telas.principal.TelaInicial;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


@Getter
@Setter
public class TelaRegrasDePreco extends TelaPadrao {
    private Painel painelRegrasDePreco;
    private TabelaPadrao tabelaRegraDePreco;
    private CaixaDeTextoPadrao caixaTextoValor;
    private CaixaDeTextoPadrao caixaTextoAno;
    private ComboBoxPadrao<Mes> escolhaMes;
    private ComboBoxPadrao<DiaDaSemana> escolhaDia;
    private ComboBoxPadrao<Turno> escolhaTurno;
    private CaixaDeTextoPadrao caixaTextoHorarioInicio;
    private CaixaDeTextoPadrao caixaTextoHorarioFim;
    private RegraDePreco regraDePrecoSelecionada = null;
    private DefaultTableModel modeloDeDados;

    public TelaRegrasDePreco() {
        super("Regras de preço", PaletaDeCores.azulForte, 1200, 670);
    }

    public void desenhar() {
        painelRegrasDePreco = new Painel(PaletaDeCores.azulForte, getXTamanho(), getYTamanho());

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.branco, "Regras de preço", 484, 40, 310, ValoresPadroes.alturaLabelTitulo);

        painelRegrasDePreco.add(labelTitulo);

        BotaoPadrao botaoVoltar = new BotaoPadrao(new ImageIcon("src/administrador/login-de-usuario.png"), 1130, 40, 50, 50);

        botaoVoltar.setToolTipText("Voltar para a tela inicial");

        botaoVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoSairPressionado) {
                dispose();
                new TelaInicial();
            }
        });

        painelRegrasDePreco.add(botaoVoltar);

        LabelSubTitulo listasDeRegras = new LabelSubTitulo(PaletaDeCores.branco, "Lista de regras", 20, 135, 545, ValoresPadroes.alturaLabelSubTitulo);

        painelRegrasDePreco.add(listasDeRegras);

        BotaoPadrao botaoAdicionarListaDePreco = new BotaoPadrao(new ImageIcon("src/administrador/login-de-usuario.png"), 707, 135, 28, 28);

        botaoAdicionarListaDePreco.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoAdicionarListaDePrecoPressionado) {

                dispose();
                new TelaCadastroRegraDePreco();
            }
        });

        painelRegrasDePreco.add(botaoAdicionarListaDePreco);

        desenharTabela();

        BotaoPadrao botaoDetalharRegraSelecionada = new BotaoPadrao("Detalhar regra selecionada", PaletaDeCores.amarelo, PaletaDeCores.azulForte, 483, 439, 252);

        botaoDetalharRegraSelecionada.addActionListener(new OuvinteBotaoDetalharRegraSelecionada(this));

        painelRegrasDePreco.add(botaoDetalharRegraSelecionada);

        LabelSubTitulo labelDetalhamento = new LabelSubTitulo(PaletaDeCores.branco, "Detalhamento", 780, 135, 400, 28);

        painelRegrasDePreco.add(labelDetalhamento);

        desenharPainelDetalhamento();

        add(painelRegrasDePreco, new GridBagConstraints());
    }

    private void desenharTabela() {
        modeloDeDados = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };

        modeloDeDados.addColumn("#");
        modeloDeDados.addColumn("Descrição");
        modeloDeDados.addColumn("Valor por hora");

        preencherDadosDaTabela();

        tabelaRegraDePreco = new TabelaPadrao(modeloDeDados);

        int larguraScroll = 715;

        tabelaRegraDePreco.getColumnModel().getColumn(0).setPreferredWidth((int) (larguraScroll * 0.10));
        tabelaRegraDePreco.getColumnModel().getColumn(1).setPreferredWidth((int) (larguraScroll * 0.70));
        tabelaRegraDePreco.getColumnModel().getColumn(2).setPreferredWidth((int) (larguraScroll * 0.20));

        JScrollPane conteinerComScroll = new JScrollPane(tabelaRegraDePreco);

        conteinerComScroll.setBounds(20, 173, larguraScroll, 243);

        painelRegrasDePreco.add(conteinerComScroll);
    }

    private void preencherDadosDaTabela() {
        List<RegraDePreco> todasAsRegras = Sessao.getDaoRegraDePreco().buscarTodos();

        modeloDeDados.setNumRows(0);

        for (RegraDePreco regra : todasAsRegras) {
            Object[] linha = {regra.getId(), regra, String.format("R$ %.2f", regra.getValorPorHora())};

            modeloDeDados.addRow(linha);
        }
    }

    private void desenharPainelDetalhamento() {
        Painel painelDetalhamento = new Painel(PaletaDeCores.branco, 780, 173, 400, 486);

        LabelPadrao labelValor = new LabelPadrao("Valor *", 45, 45, 825, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelValor);

        caixaTextoValor = new CaixaDeTextoPadrao(128, 40, 227);
        painelDetalhamento.add(caixaTextoValor);

        LabelPadrao labelAno = new LabelPadrao("Ano", 45, 94, 825, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelAno);

        caixaTextoAno = new CaixaDeTextoPadrao(128, 89, 227);
        painelDetalhamento.add(caixaTextoAno);

        LabelPadrao labelMes = new LabelPadrao("Mês", 45, 144, 825, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelMes);

        escolhaMes = new ComboBoxPadrao<Mes>(Mes.values(), 128, 138, 227, ValoresPadroes.alturaCaixasDeTexto);
        painelDetalhamento.add(escolhaMes);

        LabelPadrao labelDia = new LabelPadrao("Dia", 45, 193, 825, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelDia);

        escolhaDia = new ComboBoxPadrao<DiaDaSemana>(DiaDaSemana.values(), 128, 187, 227, ValoresPadroes.alturaCaixasDeTexto);
        painelDetalhamento.add(escolhaDia);

        LabelPadrao labelTurno = new LabelPadrao("Turno", 45, 242, 825, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelTurno);

        escolhaTurno = new ComboBoxPadrao<Turno>(Turno.values(), 128, 236, 227, ValoresPadroes.alturaCaixasDeTexto);
        painelDetalhamento.add(escolhaTurno);

        LabelPadrao labelHorario = new LabelPadrao("Horário", 45, 291, 74, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelHorario);

        caixaTextoHorarioInicio = new CaixaDeTextoPadrao(128, 285, 86);
        painelDetalhamento.add(caixaTextoHorarioInicio);

        LabelPadrao labelAte = new LabelPadrao("Até", 1003 - 780, 458 - 173, 39, 32);
        labelAte.setHorizontalAlignment(LabelPadrao.CENTER);
        painelDetalhamento.add(labelAte);

        caixaTextoHorarioFim = new CaixaDeTextoPadrao(1049 - 780, 458 - 173, 86);
        painelDetalhamento.add(caixaTextoHorarioFim);
        caixaTextoHorarioInicio.setEnabled(false);
        caixaTextoHorarioFim.setEnabled(false);

        escolhaTurno.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent turnoSelecionado) {
                boolean possuiTurnoSelecionado = escolhaTurno.getSelectedIndex() != 0;

                caixaTextoHorarioInicio.setEnabled(possuiTurnoSelecionado);
                caixaTextoHorarioFim.setEnabled(possuiTurnoSelecionado);

                if (!possuiTurnoSelecionado) {
                    caixaTextoHorarioInicio.setText("");
                    caixaTextoHorarioFim.setText("");
                }
            }
        });

        BotaoPadrao botaoAtualizar = new BotaoPadrao("Atualizar", PaletaDeCores.azulForte, PaletaDeCores.branco, 45, 540 - 173, 171);
        botaoAtualizar.addActionListener(new OuvinteBotaoAtualizarRegraDePreco(this));
        painelDetalhamento.add(botaoAtualizar);

        BotaoPadrao botaoLimpar = new BotaoPadrao("Limpar", PaletaDeCores.marrom, PaletaDeCores.branco, 1011 - 780, 540 - 173, 125);
        botaoLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zerarDetalhamento();
                tabelaRegraDePreco.clearSelection();
            }
        });
        painelDetalhamento.add(botaoLimpar);

        BotaoPadrao botaoDeletar = new BotaoPadrao("Deletar", PaletaDeCores.vermelho, PaletaDeCores.branco, 45, 587 - 173, 310);
        botaoDeletar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (regraDePrecoSelecionada == null) {
                    JOptionPane.showMessageDialog(null, "Nenhuma regra de preço foi selecionada!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Sessao.getDaoRegraDePreco().remover(regraDePrecoSelecionada);

                JOptionPane.showMessageDialog(null, "Regra de preço removida com sucesso!", "Aviso", JOptionPane.INFORMATION_MESSAGE);

                atualizarTela();
            }
        });
        painelDetalhamento.add(botaoDeletar);

        painelRegrasDePreco.add(painelDetalhamento);
    }

    public void zerarDetalhamento() {
        regraDePrecoSelecionada = null;

        caixaTextoValor.setText("");
        caixaTextoAno.setText("");
        escolhaMes.setSelectedIndex(0);
        escolhaDia.setSelectedIndex(0);
        escolhaTurno.setSelectedIndex(0);
        caixaTextoHorarioInicio.setText("");
        caixaTextoHorarioFim.setText("");
    }

    public void atualizarTela() {
        preencherDadosDaTabela();
        zerarDetalhamento();
    }
}
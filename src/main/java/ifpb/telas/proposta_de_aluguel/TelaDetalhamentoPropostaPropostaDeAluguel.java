package ifpb.telas.proposta_de_aluguel;

import ifpb.enumeradores.Status;
import ifpb.modelo.proposta_de_aluguel.PeriodoExibicaoPeca;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.telas.Sessao;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


public class TelaDetalhamentoPropostaPropostaDeAluguel extends TelaPadrao {
    public TelaDetalhamentoPropostaPropostaDeAluguel() {
        super("Detalhamento de proposta", PaletaDeCores.branco, 916, 579);
    }

    public void desenhar() {
        PropostaDeAluguel propostaDeAluguelDetalhamento = Sessao.getPropostaDeAluguelAtualParaDetalhamento();
        PeriodoExibicaoPeca periodoReferencia = propostaDeAluguelDetalhamento.getPeriodosDeTempoExibicao().get(0);

        Painel painelDetalhamento = new Painel(PaletaDeCores.branco, getXTamanho(), getYTamanho());

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Detalhamento de proposta", 245, 35, 427, ValoresPadroes.alturaLabelTitulo);
        painelDetalhamento.add(labelTitulo);

        LabelPadrao labelID = new LabelPadrao("ID", 35, 126, 152, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelID);

        LabelPadrao labelResultadoID = new LabelPadrao(String.valueOf(propostaDeAluguelDetalhamento.getId()), 202, 121, 290, 32);
        painelDetalhamento.add(labelResultadoID);

        LabelPadrao labelLocatario = new LabelPadrao("Locatario", 35, 175, 152, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelLocatario);

        LabelPadrao labelResultadoLocatario = new LabelPadrao(String.valueOf(propostaDeAluguelDetalhamento.getLocatario().getCpf()), 202, 170, 290, 32);
        painelDetalhamento.add(labelResultadoLocatario);

        LabelPadrao labelPeca = new LabelPadrao("Peça", 35, 224, 152, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelPeca);

        LabelPadrao labelResultadoPeca = new LabelPadrao(String.valueOf(propostaDeAluguelDetalhamento.getPeca().getNome()), 202, 219, 290, 32);
        painelDetalhamento.add(labelResultadoPeca);

        LabelPadrao labelPeriodo = new LabelPadrao("Períodos", 35, 273, 152, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelPeriodo);

        BotaoPadrao botaoMaisInformacoes = new BotaoPadrao("Clique aqui para mais informações", PaletaDeCores.amarelo, PaletaDeCores.azulForte, 202, 268, 290);
        botaoMaisInformacoes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoMaisInformacoesPressionado) {
                String resumoPeriodos = "Resumo dos períodos\n\n";

                int numeroPeriodo = 1;

                for (PeriodoExibicaoPeca periodo : propostaDeAluguelDetalhamento.getPeriodosDeTempoExibicao()) {
                    resumoPeriodos += String.format("Período %d: %s\n", numeroPeriodo, periodo.getPeriodoDeTempo());
                    numeroPeriodo += 1;
                }

                JOptionPane.showMessageDialog(null, resumoPeriodos, "Períodos de exibição", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        painelDetalhamento.add(botaoMaisInformacoes);

        LabelPadrao labelTurno = new LabelPadrao("Turno", 35, 322, 152, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelTurno);

        LabelPadrao labelResultadoTurno = new LabelPadrao(String.valueOf(periodoReferencia.getTurno()), 202, 317, 290, 32);
        painelDetalhamento.add(labelResultadoTurno);

        LabelPadrao labelHorario = new LabelPadrao("Horario", 35, 371, 152, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelHorario);

        LabelPadrao labelResultadoHorario = new LabelPadrao(String.valueOf(periodoReferencia.getIntervaloDeHorario()), 202, 366, 290, 32);
        painelDetalhamento.add(labelResultadoHorario);

        LabelPadrao labelValorTicket = new LabelPadrao("Valor do ticket", 35, 420, 152, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelValorTicket);

        LabelPadrao labelResultadoValorTicket = new LabelPadrao("R$ " + String.format("%.2f", propostaDeAluguelDetalhamento.getPrecoTicket()), 202, 415, 290, 32);
        painelDetalhamento.add(labelResultadoValorTicket);

        LabelPadrao labelStatus = new LabelPadrao("Status", 35, 469, 152, ValoresPadroes.alturaLabel);
        painelDetalhamento.add(labelStatus);

        LabelPadrao labelResultadoStatus = new LabelPadrao(String.valueOf(propostaDeAluguelDetalhamento.getStatus()), 202, 464, 290, 32);
        painelDetalhamento.add(labelResultadoStatus);

        BotaoPadrao botaoTransformarEmContratoAtivo = new BotaoPadrao("Transformar em contrato ativo", PaletaDeCores.azulForte, PaletaDeCores.branco, 558, 121, 323);
        botaoTransformarEmContratoAtivo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoEncerrarContrato) {
                JOptionPane.showMessageDialog(null, "Contrato ativo!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                Sessao.getPropostaDeAluguelAtualParaDetalhamento().setStatus(Status.ATIVO);
                Sessao.getDaoPropostaDeAluguel().atualizar(Sessao.getPropostaDeAluguelAtualParaDetalhamento());
                new TelaDetalhamentoPropostaPropostaDeAluguel();
            }
        });
        painelDetalhamento.add(botaoTransformarEmContratoAtivo);

        BotaoPadrao botaoGerarListaDePresenca = new BotaoPadrao("Gerar lista de presença", PaletaDeCores.azulForte, PaletaDeCores.branco, 558, 170, 323);
        botaoGerarListaDePresenca.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoListaDePresencaPressionado) {
                new TelaListaPresenca();
            }
        });
        painelDetalhamento.add(botaoGerarListaDePresenca);


        BotaoPadrao botaoGerarRelatorioFinanceiro = new BotaoPadrao("Gerar relatório financeiro", PaletaDeCores.azulForte, PaletaDeCores.branco, 558, 219, 323);
        botaoGerarRelatorioFinanceiro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoGerarRelatorioFinanceiroPressionado) {
                new TelaRelatorioFinanceiro();
            }
        });
        painelDetalhamento.add(botaoGerarRelatorioFinanceiro);

        BotaoPadrao botaoEstenderContrato = new BotaoPadrao("Estender contrato", PaletaDeCores.azulForte, PaletaDeCores.branco, 558, 268, 323);
        botaoEstenderContrato.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoEncerrarContrato) {
                dispose();
                new TelaEstenderContrato();
            }
        });
        painelDetalhamento.add(botaoEstenderContrato);

        BotaoPadrao botaoEncerrarContrato = new BotaoPadrao("Encerrar contrato", PaletaDeCores.vermelho, PaletaDeCores.branco, 558, 317, 323);
        botaoEncerrarContrato.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoEncerrarContratoPressionado) {
                dispose();
                new TelaEncerramentoContrato();
            }
        });
        painelDetalhamento.add(botaoEncerrarContrato);

        BotaoPadrao botaoSairDetalhamento = new BotaoPadrao("Sair do detalhamento", PaletaDeCores.amarelo, PaletaDeCores.branco, 558, 366, 323);
        botaoSairDetalhamento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoEncerrarContrato) {
                dispose();
                Sessao.setPropostaDeAluguelAtualParaDetalhamento(null);
                new TelaPropostasDeAluguel();
            }
        });
        painelDetalhamento.add(botaoSairDetalhamento);

        add(painelDetalhamento);
    }
}

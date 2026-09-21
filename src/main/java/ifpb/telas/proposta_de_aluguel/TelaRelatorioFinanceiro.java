package ifpb.telas.proposta_de_aluguel;

import ifpb.modelo.proposta_de_aluguel.ControleFinanceiroProposta;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.relatorios_ou_contratos.pdf.GeradorDeRelatorioFinanceiroPeca;
import ifpb.telas.Sessao;
import ifpb.telas.componente_personalizado.BotaoPadrao;
import ifpb.telas.componente_personalizado.LabelSubTitulo;
import ifpb.telas.componente_personalizado.Painel;
import ifpb.telas.componente_personalizado.TelaPadrao;
import ifpb.telas.configuracao.PaletaDeCores;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class TelaRelatorioFinanceiro extends TelaPadrao {
    private JTextArea areaTexto;

    public TelaRelatorioFinanceiro() {
        super("Relatório financeiro", PaletaDeCores.branco, 546, 481);
    }

    public void desenhar() {
        Painel painelRelatorioFinanceiro = new Painel(PaletaDeCores.branco, this.getXTamanho(), this.getYTamanho());

        LabelSubTitulo labelSubTituloRelatorioFinanceiro = new LabelSubTitulo(PaletaDeCores.preto, "Relatório financeiro (peça)", 35, 36, 476, 28);
        painelRelatorioFinanceiro.add(labelSubTituloRelatorioFinanceiro);

        areaTexto = new JTextArea();
        areaTexto.setBounds(35, 75, 476, 297);
        areaTexto.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(PaletaDeCores.preto), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        areaTexto.setEditable(false);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        painelRelatorioFinanceiro.add(areaTexto);

        escreverRelatorio();

        BotaoPadrao botaoExportarPDF = new BotaoPadrao("Exportar PDF", PaletaDeCores.amarelo, PaletaDeCores.branco, 35, 415, 282);
        botaoExportarPDF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoExportarPDFPressionado) {
                GeradorDeRelatorioFinanceiroPeca.gerarRelatorio(Sessao.getPropostaDeAluguelAtualParaDetalhamento());
                JOptionPane.showMessageDialog(null, "Relatório em PDF gerado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        painelRelatorioFinanceiro.add(botaoExportarPDF);

        BotaoPadrao botaoFechar = new BotaoPadrao("Fechar", PaletaDeCores.vermelho, PaletaDeCores.branco, 334, 415, 177);
        botaoFechar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoFecharPressionado) {
                dispose();
            }
        });
        painelRelatorioFinanceiro.add(botaoFechar);

        add(painelRelatorioFinanceiro);
    }

    private void escreverRelatorio() {
        PropostaDeAluguel propostaDeAluguel = Sessao.getPropostaDeAluguelAtualParaDetalhamento();

        ControleFinanceiroProposta controleFinanceiro = Sessao.getDaoVendaDeIngresso().gerarControleFinanceiroProposta(propostaDeAluguel);

        BigDecimal valorLiquido = controleFinanceiro.getValorLiquido();

        String situacaoFinanceira;

        if (valorLiquido.compareTo(BigDecimal.ZERO) >= 0) {
            situacaoFinanceira = String.format("Valor a ser repassado ao artista: R$ %.2f", valorLiquido);
        } else {
            situacaoFinanceira = String.format("Valor devido pelo artista ao teatro: R$ %.2f", valorLiquido.abs());
        }


        String mensagem = String.format("Peça: %s \n\n - Total arrecadado com ingressos: R$ %.2f \n - Valor total do aluguel: R$ %.2f \n - Valor líquido: %.2f \n\n %s",
                                        propostaDeAluguel.getPeca().getNome(), controleFinanceiro.getTotalArrecadado(), controleFinanceiro.getValorAluguel(), controleFinanceiro.getValorLiquido(), situacaoFinanceira);
        areaTexto.setText(mensagem);
    }
}

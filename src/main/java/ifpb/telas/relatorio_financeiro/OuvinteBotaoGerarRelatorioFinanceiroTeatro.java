package ifpb.telas.relatorio_financeiro;

import ifpb.excecoes.IntervaloDeDataInvalidoException;
import ifpb.modelo.intervalos.IntervaloDatas;
import ifpb.relatorios_ou_contratos.pdf.GeradorRelatorioFinanceiroTeatro;
import ifpb.telas.Sessao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class OuvinteBotaoGerarRelatorioFinanceiroTeatro implements ActionListener {
    private final TelaRelatorioFinanceiroTeatro tela;

    public OuvinteBotaoGerarRelatorioFinanceiroTeatro(TelaRelatorioFinanceiroTeatro tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoGerarRelatorioPressionado) {
        String textoDataInicio = tela.getCaixaDeTextoDataDeInicio().getText();
        String textoDataFim = tela.getCaixaTextoDataDeFim().getText();

        if (textoDataInicio.contains(" ") || textoDataFim.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Informe as duas datas!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        LocalDate dataInicio;
        LocalDate dataFim;

        try {
            DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

            dataInicio = LocalDate.parse(textoDataInicio, formatadorData);
            dataFim = LocalDate.parse(textoDataFim, formatadorData);
        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(null, "Data inválida!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        IntervaloDatas intervaloDatas;

        try {
            intervaloDatas = new IntervaloDatas(dataInicio, dataFim);
        } catch (IntervaloDeDataInvalidoException exception) {
            JOptionPane.showMessageDialog(null, "Intervalo de datas inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        BigDecimal valorTotalVendaDeIngressos = Sessao.getDaoVendaDeIngresso().calcularTotalVendaIngressosDoTeatroPorIntervalo(intervaloDatas);
        BigDecimal valorTotalAlugueis = Sessao.getDaoPropostaDeAluguel().calcularValorTotalDeAluguelDoTeatroPorIntervalo(intervaloDatas);
        BigDecimal somaDeArrecadacoesGerais = valorTotalVendaDeIngressos.add(valorTotalAlugueis);

        String resumo = String.format("PDF gerado!\n\nResumo financeiro (%s até %s)\n\n- Total com venda de ingressos: R$ %.2f\n- Total com aluguéis: R$ %.2f\n- Total geral de receitas: R$ %.2f", textoDataInicio, textoDataFim, valorTotalVendaDeIngressos, valorTotalAlugueis, somaDeArrecadacoesGerais);

        GeradorRelatorioFinanceiroTeatro.gerarRelatorio(dataInicio, dataFim, valorTotalVendaDeIngressos, valorTotalAlugueis, somaDeArrecadacoesGerais);

        JOptionPane.showMessageDialog(null, resumo, "Relatório Financeiro", JOptionPane.INFORMATION_MESSAGE);
    }
}
package ifpb.telas.proposta_de_aluguel;

import ifpb.modelo.proposta_de_aluguel.ControleFinanceiroProposta;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.telas.Sessao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class OuvinteBotaoGerarRelatorioEncerramento implements ActionListener {

    private final TelaEncerramentoContrato tela;

    public OuvinteBotaoGerarRelatorioEncerramento(TelaEncerramentoContrato tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoGerarRelatorioPressionado) {
        String textoData = tela.getCaixaDeTextoDataEncerramentoEfetivo().getText();

        if (textoData.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Informe a data de encerramento!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        LocalDate dataEncerramento;

        try {
            DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
            dataEncerramento = LocalDate.parse(textoData, formatadorData);
        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(null, "Data de encerramento inválida!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        PropostaDeAluguel propostaDeAluguel = Sessao.getPropostaDeAluguelAtualParaDetalhamento();

        if (!propostaDeAluguel.isDataEncerramentoValida(dataEncerramento)) {
            JOptionPane.showMessageDialog(null, "A data de encerramento não pertence ao período do contrato!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        if (Sessao.getDaoVendaDeIngresso().existeVendaAposData(propostaDeAluguel, dataEncerramento)) {
            JOptionPane.showMessageDialog(null, "Existem ingressos vendidos para uma data posterior ao encerramento!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        ControleFinanceiroProposta controleFinanceiro = Sessao.getDaoVendaDeIngresso().gerarControleFinanceiroProposta(propostaDeAluguel);
        BigDecimal valorTotalVendaIngressos = controleFinanceiro.getTotalArrecadado();
        BigDecimal valorTotalAluguel = propostaDeAluguel.calcularValorAluguelAteData(dataEncerramento);
        BigDecimal valorRepassado = BigDecimal.ZERO;
        BigDecimal saldoAluguel = BigDecimal.ZERO;

        if (valorTotalVendaIngressos.compareTo(valorTotalAluguel) >= 0) {
            valorRepassado = valorTotalVendaIngressos.subtract(valorTotalAluguel);
        } else {
            saldoAluguel = valorTotalAluguel.subtract(valorTotalVendaIngressos);
        }

        String resumo = String.format("Peça: %s\n" + "Data de encerramento: %s\n\n" + "Total arrecadado com ingressos: R$ %.2f\n" + "Aluguel até o encerramento: R$ %.2f\n" + "Valor a repassar ao artista: R$ %.2f\n" + "Saldo de aluguel a pagar: R$ %.2f\n\n" + "Deseja confirmar o encerramento?", propostaDeAluguel.getPeca().getNome(), dataEncerramento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), valorTotalVendaIngressos, valorTotalAluguel, valorRepassado, saldoAluguel);

        int opcao = JOptionPane.showConfirmDialog(null, resumo, "Confirmar encerramento", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (opcao != JOptionPane.OK_OPTION) {
            return;
        }

        propostaDeAluguel.registrarEncerramento(dataEncerramento, valorRepassado, saldoAluguel);

        Sessao.getDaoPropostaDeAluguel().atualizar(propostaDeAluguel);

        JOptionPane.showMessageDialog(null, "Contrato encerrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        tela.dispose();

        new TelaDetalhamentoPropostaPropostaDeAluguel();
    }
}
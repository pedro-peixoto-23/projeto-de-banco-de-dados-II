package ifpb.telas.regra_de_preco;

import ifpb.enumeradores.DiaDaSemana;
import ifpb.enumeradores.Mes;
import ifpb.enumeradores.Turno;
import ifpb.excecoes.IntervaloHorarioInvalidoException;
import ifpb.modelo.intervalos.IntervaloHorarios;
import ifpb.modelo.regra_de_preco.RegraDePreco;
import ifpb.telas.Sessao;
import ifpb.telas.configuracao.ValidadorDeDados;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class OuvinteBotaoAtualizarRegraDePreco implements ActionListener {
    private final TelaRegrasDePreco tela;

    public OuvinteBotaoAtualizarRegraDePreco(TelaRegrasDePreco tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoAtualizarRegraDePrecoPressionado) {
        RegraDePreco regraDePrecoSelecionada = tela.getRegraDePrecoSelecionada();
        String txtValorPorHora = tela.getCaixaTextoValor().getText().trim();

        if (regraDePrecoSelecionada == null) {
            JOptionPane.showMessageDialog(null, "Nenhuma regra de preço foi selecionada!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (txtValorPorHora.isBlank()) {
            JOptionPane.showMessageDialog(null, "Valor por hora é obrigatório!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal valorPorHora;

        try {
            valorPorHora = new BigDecimal(txtValorPorHora);

            if (valorPorHora.compareTo(BigDecimal.ZERO) < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(null, "Valor por hora inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer ano = null;
        Mes mes = null;
        DiaDaSemana diaDaSemana = null;
        Turno turno = null;
        IntervaloHorarios intervaloDeHorario = null;

        String txtAno = tela.getCaixaTextoAno().getText().trim();

        if (!txtAno.isBlank()) {
            if (!ValidadorDeDados.isAnoValido(txtAno)) {
                JOptionPane.showMessageDialog(null, "Ano inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ano = Integer.parseInt(txtAno);
        }

        if (tela.getEscolhaMes().getSelectedIndex() != 0) {
            mes = (Mes) tela.getEscolhaMes().getSelectedItem();
        }

        if (tela.getEscolhaDia().getSelectedIndex() != 0) {
            diaDaSemana = (DiaDaSemana) tela.getEscolhaDia().getSelectedItem();
        }

        if (tela.getEscolhaTurno().getSelectedIndex() != 0) {
            turno = (Turno) tela.getEscolhaTurno().getSelectedItem();
        }

        String horarioInicio = tela.getCaixaTextoHorarioInicio().getText().trim();
        String horarioFim = tela.getCaixaTextoHorarioFim().getText().trim();

        boolean possuiHorarioPreenchido = !horarioInicio.isBlank() || !horarioFim.isBlank();

        if (possuiHorarioPreenchido && turno == null) {
            JOptionPane.showMessageDialog(null, "Selecione um turno antes de informar um horário!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (possuiHorarioPreenchido && (horarioInicio.isBlank() || horarioFim.isBlank())) {
            JOptionPane.showMessageDialog(null, "Informe o horário inicial e o horário final!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (possuiHorarioPreenchido) {
            try {
                intervaloDeHorario = new IntervaloHorarios(LocalTime.parse(horarioInicio), LocalTime.parse(horarioFim));

                if (!intervaloDeHorario.isDentro(turno.getIntervaloDeHorario())) {
                    JOptionPane.showMessageDialog(null, "O horário informado deve estar dentro do turno selecionado (" + turno.getIntervaloDeHorario().getHorarioInicio() + " até " + turno.getIntervaloDeHorario().getHorarioFim() + ").", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (DateTimeParseException exception) {
                JOptionPane.showMessageDialog(null, "Horários inseridos inválidos. Insira no formato: HH:mm.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            } catch (IntervaloHorarioInvalidoException exception) {
                JOptionPane.showMessageDialog(null, "O intervalo inserido é inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        RegraDePreco regraAtualizada = new RegraDePreco(valorPorHora, ano, mes, diaDaSemana, turno, intervaloDeHorario);

        if (regraDePrecoSelecionada.temMesmaConfiguracao(regraAtualizada)) {
            JOptionPane.showMessageDialog(null, "Dados não foram alterados", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (Sessao.getDaoRegraDePreco().existeRegraComMesmaConfiguracao(regraAtualizada)) {
            JOptionPane.showMessageDialog(null, "Regra de preço já existente!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        regraDePrecoSelecionada.atualizarRegra(valorPorHora, ano, mes, diaDaSemana, turno, intervaloDeHorario);

        Sessao.getDaoRegraDePreco().atualizar(regraDePrecoSelecionada);

        JOptionPane.showMessageDialog(null, "Regra de preço atualizada com sucesso!", "Aviso", JOptionPane.INFORMATION_MESSAGE);

        tela.atualizarTela();
    }
}
package ifpb.telas.regra_de_preco;

import ifpb.enumeradores.DiaDaSemana;
import ifpb.enumeradores.Mes;
import ifpb.enumeradores.Turno;
import ifpb.excecoes.IntervaloHorarioInvalidoException;
import ifpb.modelo.intervalos.IntervaloHorarios;
import ifpb.modelo.regra_de_preco.RegraDePreco;
import ifpb.telas.Sessao;
import ifpb.telas.configuracao.ValidadorDeDados;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;

public class OuvinteBotaoCadastrarRegraDePreco implements ActionListener {

    private final TelaCadastroRegraDePreco tela;

    public OuvinteBotaoCadastrarRegraDePreco(TelaCadastroRegraDePreco tela) {
        this.tela = tela;
    }

    @Override
    public void actionPerformed(ActionEvent botaoCadastrarRegraDePrecoPressionado) {
        String txtValorPorHora = tela.getCaixaTextoValor().getText().trim();
        String txtAno = tela.getCaixaTextoAno().getText().trim();

        int indiceEscolhaMes = tela.getEscolhaMes().getSelectedIndex();
        int indiceEscolhaDia = tela.getEscolhaDia().getSelectedIndex();
        int indiceEscolhaTurno = tela.getEscolhaTurno().getSelectedIndex();

        String horarioInicio = tela.getCaixaTextoHorarioInicio().getText().trim();
        String horarioFim = tela.getCaixaTextoHorarioFim().getText().trim();

        BigDecimal valorPorHora;

        if (txtValorPorHora.isBlank()) {
            JOptionPane.showMessageDialog(null, "Valor por hora é obrigatório!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

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

        if (!txtAno.isBlank()) {
            if (!ValidadorDeDados.isAnoValido(txtAno)) {
                JOptionPane.showMessageDialog(null, "Ano inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ano = Integer.parseInt(txtAno);
        }

        if (indiceEscolhaMes != 0) {
            mes = Mes.values()[indiceEscolhaMes];
        }

        if (indiceEscolhaDia != 0) {
            diaDaSemana = DiaDaSemana.values()[indiceEscolhaDia];
        }

        if (indiceEscolhaTurno != 0) {
            turno = Turno.values()[indiceEscolhaTurno];
        }

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
                JOptionPane.showMessageDialog(null, "Horário inválido inserido. Insira no formato: HH:mm.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            } catch (IntervaloHorarioInvalidoException exception) {
                JOptionPane.showMessageDialog(null, "O intervalo inserido é inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        RegraDePreco regraDePreco = new RegraDePreco(valorPorHora, ano, mes, diaDaSemana, turno, intervaloDeHorario);

        if (Sessao.getDaoRegraDePreco().existeRegraComMesmaConfiguracao(regraDePreco)) {
            JOptionPane.showMessageDialog(null, "Regra de preço já existente!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Sessao.getDaoRegraDePreco().salvar(regraDePreco);

        JOptionPane.showMessageDialog(null, "Regra cadastrada com sucesso!", "Aviso", JOptionPane.INFORMATION_MESSAGE);

        tela.dispose();
        new TelaRegrasDePreco();
    }
}
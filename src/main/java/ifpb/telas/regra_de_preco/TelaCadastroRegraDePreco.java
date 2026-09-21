package ifpb.telas.regra_de_preco;

import ifpb.enumeradores.DiaDaSemana;
import ifpb.enumeradores.Mes;
import ifpb.enumeradores.Turno;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import lombok.Getter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
public class TelaCadastroRegraDePreco extends TelaPadrao {
    private CaixaDeTextoPadrao caixaTextoValor;
    private CaixaDeTextoPadrao caixaTextoAno;
    private ComboBoxPadrao<Mes> escolhaMes;
    private ComboBoxPadrao<DiaDaSemana> escolhaDia;
    private ComboBoxPadrao<Turno> escolhaTurno;
    private CaixaDeTextoPadrao caixaTextoHorarioInicio;
    private CaixaDeTextoPadrao caixaTextoHorarioFim;

    public TelaCadastroRegraDePreco() {
        super("Cadastro (Regra de preço)", PaletaDeCores.branco, 756, 450);
    }

    public void desenhar() {
        Painel painelCadastro = new Painel(PaletaDeCores.branco, this.getXTamanho(), this.getYTamanho());

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Cadastro (Regra de preço)", 35, 35, 686, ValoresPadroes.alturaLabelTitulo);
        painelCadastro.add(labelTitulo);

        LabelPadrao labelValor = new LabelPadrao("Valor *", 35, 120, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelValor);

        caixaTextoValor = new CaixaDeTextoPadrao(35, 146, 310);
        painelCadastro.add(caixaTextoValor);

        LabelPadrao labelAno = new LabelPadrao("Ano", 411, 120, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelAno);

        caixaTextoAno = new CaixaDeTextoPadrao(411, 146, 310);
        painelCadastro.add(caixaTextoAno);

        LabelPadrao labelMes = new LabelPadrao("Mês", 35, 201, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelMes);

        escolhaMes = new ComboBoxPadrao<>(Mes.values(), 35, 227, 310, ValoresPadroes.alturaCaixasDeTexto);
        painelCadastro.add(escolhaMes);

        LabelPadrao labelDia = new LabelPadrao("Dia", 411, 201, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelDia);

        escolhaDia = new ComboBoxPadrao<DiaDaSemana>(DiaDaSemana.values(), 411, 227, 310, ValoresPadroes.alturaCaixasDeTexto);
        painelCadastro.add(escolhaDia);

        LabelPadrao labelTurno = new LabelPadrao("Turno", 35, 282, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelTurno);

        escolhaTurno = new ComboBoxPadrao<Turno>(Turno.values(), 35, 308, 310, ValoresPadroes.alturaCaixasDeTexto);
        painelCadastro.add(escolhaTurno);

        LabelPadrao labelHorario = new LabelPadrao("Horário", 411, 282, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelHorario);

        caixaTextoHorarioInicio = new CaixaDeTextoPadrao(411, 308, 86);
        painelCadastro.add(caixaTextoHorarioInicio);

        LabelPadrao labelAte = new LabelPadrao("Até", 505, 314, 39, ValoresPadroes.alturaLabel);
        labelAte.setHorizontalAlignment(LabelPadrao.CENTER);
        painelCadastro.add(labelAte);

        caixaTextoHorarioFim = new CaixaDeTextoPadrao(552, 308, 86);
        painelCadastro.add(caixaTextoHorarioFim);

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

        BotaoPadrao botaoCadastrar = new BotaoPadrao("Cadastrar", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 383, 472);
        botaoCadastrar.addActionListener(new OuvinteBotaoCadastrarRegraDePreco(this));
        painelCadastro.add(botaoCadastrar);

        BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 520, 383, 201);
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoCancelarPressionado) {
                dispose();
                new TelaRegrasDePreco();
            }
        });

        painelCadastro.add(botaoCancelar);

        add(painelCadastro);
    }
}
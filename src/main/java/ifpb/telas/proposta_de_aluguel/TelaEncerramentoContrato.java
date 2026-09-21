package ifpb.telas.proposta_de_aluguel;

import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import lombok.Getter;

import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

@Getter
public class TelaEncerramentoContrato extends TelaPadrao {
    private CaixaDeTextoComMascara caixaDeTextoDataEncerramentoEfetivo;

    public TelaEncerramentoContrato() {
        super("Encerramento de contrato", PaletaDeCores.branco, 380, 288);
    }

    public void desenhar() {
        Painel painelEncerramentoContrato = new Painel(PaletaDeCores.branco, getXTamanho(), getYTamanho());

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Encerramento", 35, 35, 310, ValoresPadroes.alturaLabelTitulo);
        painelEncerramentoContrato.add(labelTitulo);

        LabelPadrao labelDataEncerramentoEfetivo = new LabelPadrao("Data de encerramento efetivo", 35, 120, 310, ValoresPadroes.alturaLabel);
        painelEncerramentoContrato.add(labelDataEncerramentoEfetivo);

        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter(' ');

            caixaDeTextoDataEncerramentoEfetivo = new CaixaDeTextoComMascara(mascaraData, 35, 146, 310, ValoresPadroes.alturaCaixasDeTexto);
            painelEncerramentoContrato.add(caixaDeTextoDataEncerramentoEfetivo);
        } catch (ParseException exception) {
            throw new RuntimeException(exception);
        }

        BotaoPadrao botaoGerarRelatorio = new BotaoPadrao("Gerar Relatório", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 221, 186);
        botaoGerarRelatorio.addActionListener(new OuvinteBotaoGerarRelatorioEncerramento(this));
        painelEncerramentoContrato.add(botaoGerarRelatorio);

        BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 232, 221, 113);
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoCancelarPressionado) {
                dispose();
                new TelaDetalhamentoPropostaPropostaDeAluguel();
            }
        });
        painelEncerramentoContrato.add(botaoCancelar);

        add(painelEncerramentoContrato);
    }
}

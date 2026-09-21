package ifpb.telas.relatorio_financeiro;

import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import ifpb.telas.principal.TelaInicial;
import lombok.Getter;

import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

@Getter
public class TelaRelatorioFinanceiroTeatro extends TelaPadrao {
    private CaixaDeTextoComMascara caixaDeTextoDataDeInicio;
    private CaixaDeTextoComMascara CaixaTextoDataDeFim;

    public TelaRelatorioFinanceiroTeatro() {
        super("Relatório Financeiro", PaletaDeCores.branco, 380, 369);
    }

    public void desenhar() {
        Painel painelRelatorioFinanceiro = new Painel(PaletaDeCores.branco, getXTamanho(), getYTamanho());

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Rel. Financeiro", 35, 35, 310, ValoresPadroes.alturaLabelTitulo);
        painelRelatorioFinanceiro.add(labelTitulo);

        try {
            MaskFormatter mascaraDataInicio = new MaskFormatter("##/##/####");
            mascaraDataInicio.setPlaceholderCharacter(' ');

            MaskFormatter mascaraDataFim = new MaskFormatter("##/##/####");
            mascaraDataFim.setPlaceholderCharacter(' ');

            LabelPadrao labelDataDeInicio = new LabelPadrao("Data de início", 35, 120, 310, ValoresPadroes.alturaLabel);
            painelRelatorioFinanceiro.add(labelDataDeInicio);

            caixaDeTextoDataDeInicio = new CaixaDeTextoComMascara(mascaraDataInicio, 35, 146, 310, ValoresPadroes.alturaCaixasDeTexto);
            painelRelatorioFinanceiro.add(caixaDeTextoDataDeInicio);

            LabelPadrao labelDataFinal = new LabelPadrao("Data final", 35, 201, 310, ValoresPadroes.alturaLabel);
            painelRelatorioFinanceiro.add(labelDataFinal);

            CaixaTextoDataDeFim = new CaixaDeTextoComMascara(mascaraDataFim, 35, 227, 310, ValoresPadroes.alturaCaixasDeTexto);
            painelRelatorioFinanceiro.add(CaixaTextoDataDeFim);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        BotaoPadrao botaoGerarRelatorioFinanceiro = new BotaoPadrao("Gerar Relatório", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 302, 186);
        botaoGerarRelatorioFinanceiro.addActionListener(new OuvinteBotaoGerarRelatorioFinanceiroTeatro(this));
        painelRelatorioFinanceiro.add(botaoGerarRelatorioFinanceiro);

        BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 232, 302, 113);
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoCancelarPressionado) {
                dispose();
                new TelaInicial();
            }
        });
        painelRelatorioFinanceiro.add(botaoCancelar);

        add(painelRelatorioFinanceiro);
    }
}

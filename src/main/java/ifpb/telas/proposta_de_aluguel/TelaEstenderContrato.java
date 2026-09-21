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
public class TelaEstenderContrato extends TelaPadrao {
    private CaixaDeTextoComMascara caixaDeTextoDataDeInicio;
    private CaixaDeTextoComMascara CaixaTextoDataDeFim;

    public TelaEstenderContrato() {
        super("Estender contrato", PaletaDeCores.branco, 380, 369);
    }

    public void desenhar() {
        Painel painelEstenderContrato = new Painel(PaletaDeCores.branco, getXTamanho(), getYTamanho());

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Estender", 35, 35, 310, ValoresPadroes.alturaLabelTitulo);
        painelEstenderContrato.add(labelTitulo);

        try {
            MaskFormatter mascaraDataInicio = new MaskFormatter("##/##/####");
            mascaraDataInicio.setPlaceholderCharacter(' ');

            MaskFormatter mascaraDataFim = new MaskFormatter("##/##/####");
            mascaraDataFim.setPlaceholderCharacter(' ');

            LabelPadrao labelDataDeInicio = new LabelPadrao("Data de início", 35, 120, 310, ValoresPadroes.alturaLabel);
            painelEstenderContrato.add(labelDataDeInicio);

            caixaDeTextoDataDeInicio = new CaixaDeTextoComMascara(mascaraDataInicio, 35, 146, 310, ValoresPadroes.alturaCaixasDeTexto);
            painelEstenderContrato.add(caixaDeTextoDataDeInicio);

            LabelPadrao labelDataFinal = new LabelPadrao("Data final", 35, 201, 310, ValoresPadroes.alturaLabel);
            painelEstenderContrato.add(labelDataFinal);

            CaixaTextoDataDeFim = new CaixaDeTextoComMascara(mascaraDataFim, 35, 227, 310, ValoresPadroes.alturaCaixasDeTexto);
            painelEstenderContrato.add(CaixaTextoDataDeFim);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        BotaoPadrao botaoAplicarExtensao = new BotaoPadrao("Aplicar Extensão", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 302, 186);
        botaoAplicarExtensao.addActionListener(new OuvinteBotaoAplicarExtensao(this));
        painelEstenderContrato.add(botaoAplicarExtensao);

        BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 232, 302, 113);
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoCancelarPressionado) {
                dispose();
                new TelaDetalhamentoPropostaPropostaDeAluguel();
            }
        });
        painelEstenderContrato.add(botaoCancelar);

        add(painelEstenderContrato);
    }
}

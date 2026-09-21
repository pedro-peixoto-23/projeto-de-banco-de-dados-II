package ifpb.telas.venda_de_ingresso;

import ifpb.modelo.proposta_de_aluguel.ControleValorDiarioAluguel;
import ifpb.modelo.proposta_de_aluguel.PeriodoExibicaoPeca;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.telas.Sessao;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import ifpb.telas.principal.TelaInicial;
import lombok.Getter;

import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@Getter
public class TelaVendaDeIngresso extends TelaPadrao {
    Painel painelCadastro;
    private CaixaDeTextoComMascara caixaTextoCPF;
    private ComboBoxPadrao<PropostaDeAluguel> escolhaPeca;
    private ComboBoxPadrao<LocalDate> escolhaDataPeca;
    private CaixaDeTextoPadrao caixaTextoQtdTicket;


    public TelaVendaDeIngresso() {
        super("Venda de ingresso", PaletaDeCores.branco, 756, 662);
    }

    public void desenhar() {
        painelCadastro = new Painel(PaletaDeCores.branco, this.getXTamanho(), this.getYTamanho());

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Venda de ingresso", 35, 35, 686, ValoresPadroes.alturaLabelTitulo);
        painelCadastro.add(labelTitulo);

        LabelSubtituloFormularios labelSubtituloDadosLocatario = new LabelSubtituloFormularios(Color.black, "Dados do cliente", 35, 120, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelSubtituloDadosLocatario);

        try {
            MaskFormatter mascaraCPF = new MaskFormatter("###.###.###-##");
            mascaraCPF.setPlaceholderCharacter(' ');

            LabelPadrao labelCPF = new LabelPadrao("CPF", 35, 175, 310, ValoresPadroes.alturaLabel);
            painelCadastro.add(labelCPF);

            caixaTextoCPF = new CaixaDeTextoComMascara(mascaraCPF, 35, 201, 310, ValoresPadroes.alturaCaixasDeTexto);
            painelCadastro.add(caixaTextoCPF);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        BotaoPadrao botaoDetalhar = new BotaoPadrao("Ver detalhes do cliente", PaletaDeCores.amarelo, PaletaDeCores.azulForte, 411, 201, 310);
        painelCadastro.add(botaoDetalhar);

        desenharSelecoes();

        LabelSubtituloFormularios labelSubtituloDadosDaPeca = new LabelSubtituloFormularios(Color.black, "Dados da peça", 35, 277, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelSubtituloDadosDaPeca);

        LabelPadrao labelQtdTicket = new LabelPadrao("Qtd. tickets", 35, 494, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelQtdTicket);

        caixaTextoQtdTicket = new CaixaDeTextoPadrao(35, 520, 310);
        painelCadastro.add(caixaTextoQtdTicket);

        BotaoPadrao botaoCadastrar = new BotaoPadrao("Cadastrar", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 595, 472);
        botaoCadastrar.addActionListener(new OuvinteBotaoCadastrarVendaIngresso(this));
        painelCadastro.add(botaoCadastrar);

        BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 520, 595, 201);
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoSairPressionado) {
                dispose();
                new TelaInicial();
            }
        });
        painelCadastro.add(botaoCancelar);

        add(painelCadastro);
    }

    public void desenharSelecoes() {
        List<PropostaDeAluguel> propostasAtivas = Sessao.getDaoPropostaDeAluguel().buscarAtivas();

        PropostaDeAluguel[] propostasAtivasConvertidas = propostasAtivas.toArray(new PropostaDeAluguel[0]);

        LabelPadrao labelPeca = new LabelPadrao("Peça", 35, 332, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelPeca);

        escolhaPeca = new ComboBoxPadrao<PropostaDeAluguel>(propostasAtivasConvertidas, 35, 358, 310, ValoresPadroes.alturaCaixasDeTexto);
        escolhaPeca.setSelectedIndex(-1);
        escolhaPeca.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                PropostaDeAluguel propostaSelecionada = (PropostaDeAluguel) escolhaPeca.getSelectedItem();

                escolhaDataPeca.removeAllItems();

                if (propostaSelecionada == null) {
                    return;
                }

                for (PeriodoExibicaoPeca periodo : propostaSelecionada.getPeriodosDeTempoExibicao()) {
                    for (ControleValorDiarioAluguel controle : periodo.getListaControleValorDiarioAluguel()) {
                        escolhaDataPeca.addItem(controle.getDia());
                    }
                }
            }
        });
        painelCadastro.add(escolhaPeca);

        LabelPadrao labelDataPeca = new LabelPadrao("Data da peça", 35, 413, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelDataPeca);

        escolhaDataPeca = new ComboBoxPadrao<LocalDate>(new LocalDate[0], 35, 439, 310, ValoresPadroes.alturaCaixasDeTexto);
        painelCadastro.add(escolhaDataPeca);
    }
}
	
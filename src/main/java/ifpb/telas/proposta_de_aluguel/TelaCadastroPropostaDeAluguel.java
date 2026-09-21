package ifpb.telas.proposta_de_aluguel;

import ifpb.enumeradores.Turno;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import lombok.Getter;

import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;


@Getter
public class TelaCadastroPropostaDeAluguel extends TelaPadrao {
	private CaixaDeTextoComMascara caixaTextoCPF;
	private CaixaDeTextoPadrao caixaTextoNome;
	private CaixaDeTextoComMascara caixaTextoDataInicio;
	private CaixaDeTextoComMascara caixaTextoDataFim;
	private ComboBoxPadrao<Turno> escolhaTurno;
	private CaixaDeTextoComMascara caixaTextoHorarioInicio;
	private CaixaDeTextoComMascara caixaTextoHorarioFim;
	private	CaixaDeTextoPadrao caixaTextoValorTicket;


	public TelaCadastroPropostaDeAluguel() {
		super("Cadastro (Proposta de aluguel)", PaletaDeCores.branco, 756, 662);
	}
	
	public void desenhar() {
		Painel painelCadastro = new Painel(PaletaDeCores.branco, this.getXTamanho(), this.getYTamanho());
		
		LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Cadastro (Proposta de aluguel)", 35, 35, 686, ValoresPadroes.alturaLabelTitulo);
		painelCadastro.add(labelTitulo);
		
		LabelSubtituloFormularios labelSubtituloDadosLocatario = new LabelSubtituloFormularios(Color.black, "Dados do locatário", 35, 120, 310, ValoresPadroes.alturaLabel);
		painelCadastro.add(labelSubtituloDadosLocatario);
		
		try {
			MaskFormatter mascaraCPF = new MaskFormatter("###.###.###-##");
			MaskFormatter mascaraData = new MaskFormatter("##/##/####");
			MaskFormatter mascaraHorario = new MaskFormatter("##:##");
			mascaraCPF.setPlaceholderCharacter(' ');
			mascaraData.setPlaceholderCharacter(' ');
			mascaraHorario.setPlaceholderCharacter(' ');

			
			LabelPadrao labelCPF = new LabelPadrao("CPF", 35, 175, 310, ValoresPadroes.alturaLabel);
			painelCadastro.add(labelCPF);

			caixaTextoCPF = new CaixaDeTextoComMascara(mascaraCPF, 35, 201, 310, ValoresPadroes.alturaCaixasDeTexto);
			painelCadastro.add(caixaTextoCPF);

			LabelPadrao labelDataInicio = new LabelPadrao("Data de início", 35, 413, 310, ValoresPadroes.alturaLabel);
			painelCadastro.add(labelDataInicio);
			
			caixaTextoDataInicio = new CaixaDeTextoComMascara(mascaraData, 35, 439, 310, ValoresPadroes.alturaCaixasDeTexto);
			painelCadastro.add(caixaTextoDataInicio);

			LabelPadrao labelDataFim = new LabelPadrao("Data de fim", 35, 494, 310, ValoresPadroes.alturaLabel);
			painelCadastro.add(labelDataFim);

			caixaTextoDataFim = new CaixaDeTextoComMascara(mascaraData, 35, 520, 310, ValoresPadroes.alturaCaixasDeTexto);
			painelCadastro.add(caixaTextoDataFim);

			LabelPadrao labelHorario = new LabelPadrao("Horário", 411, 413, 310, ValoresPadroes.alturaLabel);
			painelCadastro.add(labelHorario);
			
			caixaTextoHorarioInicio = new CaixaDeTextoComMascara(mascaraHorario, 411, 439, 86, ValoresPadroes.alturaCaixasDeTexto);
			painelCadastro.add(caixaTextoHorarioInicio);

			LabelPadrao labelAte = new LabelPadrao("Até", 505, 445, 39, ValoresPadroes.alturaLabel);
			labelAte.setHorizontalAlignment(LabelPadrao.CENTER);
			painelCadastro.add(labelAte);

			caixaTextoHorarioFim = new CaixaDeTextoComMascara(mascaraHorario, 552, 439, 86, ValoresPadroes.alturaCaixasDeTexto);
			painelCadastro.add(caixaTextoHorarioFim);
		} catch (ParseException exception) {
			exception.printStackTrace();
		}
		
		BotaoPadrao botaoDetalhar = new BotaoPadrao("Ver detalhes do locatário", PaletaDeCores.amarelo, PaletaDeCores.azulForte, 411, 201, 310);
		painelCadastro.add(botaoDetalhar);
		
		LabelSubtituloFormularios labelSubtituloDadosDaPeca = new LabelSubtituloFormularios(Color.black, "Dados da peça", 35, 277, 310, ValoresPadroes.alturaLabel);
		painelCadastro.add(labelSubtituloDadosDaPeca);
		
		LabelPadrao labelNome = new LabelPadrao("Nome", 35, 332, 310, ValoresPadroes.alturaLabel);
		painelCadastro.add(labelNome);
		
		caixaTextoNome = new CaixaDeTextoPadrao(35, 358, 310);
		painelCadastro.add(caixaTextoNome);

		LabelPadrao labelTurno = new LabelPadrao("Turno", 411, 332, 310, ValoresPadroes.alturaLabel);
		painelCadastro.add(labelTurno);
		
		escolhaTurno = new ComboBoxPadrao<>(Turno.values(), 411, 358, 310, ValoresPadroes.alturaCaixasDeTexto);
		painelCadastro.add(escolhaTurno);

		LabelPadrao labelValorTicket = new LabelPadrao("Valor do ticket", 411, 494, 310, ValoresPadroes.alturaLabel);
		painelCadastro.add(labelValorTicket);
		
		caixaTextoValorTicket = new CaixaDeTextoPadrao(411, 520, 310);
		painelCadastro.add(caixaTextoValorTicket);

		BotaoPadrao botaoCadastrar = new BotaoPadrao("Cadastrar", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 595, 472);
		botaoCadastrar.addActionListener(new OuvinteBotaoCadastrarPropostaDeAluguel(this));
		painelCadastro.add(botaoCadastrar);

		BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 520, 595, 201);
		botaoCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent botaoSairPressionado) {
				dispose();
				new TelaPropostasDeAluguel();
			}
		});
		painelCadastro.add(botaoCancelar);
		
		add(painelCadastro);
	}
}
	
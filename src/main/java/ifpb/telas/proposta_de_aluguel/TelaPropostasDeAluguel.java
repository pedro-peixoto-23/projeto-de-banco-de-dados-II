package ifpb.telas.proposta_de_aluguel;

import ifpb.dao.DaoPropostaDeAluguel;
import ifpb.enumeradores.Status;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.telas.Sessao;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import ifpb.telas.principal.TelaInicial;
import lombok.Getter;
import lombok.Setter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

@Getter
@Setter
public class TelaPropostasDeAluguel extends TelaPadrao {
	private Painel painelPropostasDeAluguel;
	private TabelaPadrao tabelaPropostasDeAluguel;
	private DefaultTableModel modeloDeDados;
	private ComboBoxPadrao<Status> escolhaStatus;
	private CaixaDeTextoPadrao caixaDeTextoNomeLocatario;
	private CaixaDeTextoPadrao caixaDeTextoNomePeca;
	private DaoPropostaDeAluguel daoPropostaDeAluguel;
	
	public TelaPropostasDeAluguel() {
		super("Propostas de aluguel", PaletaDeCores.azulForte, 1200, 670);
	}
	
	public void desenhar() {
		painelPropostasDeAluguel = new Painel(PaletaDeCores.azulForte, getXTamanho(), getYTamanho());
		
		LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.branco, "Propostas de aluguel", 485, 40, 310, ValoresPadroes.alturaLabelTitulo);
		painelPropostasDeAluguel.add(labelTitulo);
		
		BotaoPadrao botaoHome = new BotaoPadrao(new ImageIcon("src/administrador/login-de-usuario.png"), 1130, 40, 50, 50);
		botaoHome.setToolTipText("Voltar para a tela inicial");
		botaoHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent botaoSairPressionado) {
				dispose();
				new TelaInicial();
			}
		});
		painelPropostasDeAluguel.add(botaoHome);
		
		LabelSubTitulo labelListaDePropostas = new LabelSubTitulo(PaletaDeCores.branco, "Lista de propostas", 20, 135, 527, ValoresPadroes.alturaLabelSubTitulo);
		painelPropostasDeAluguel.add(labelListaDePropostas);
		
		BotaoPadrao botaoAdicionarPropostaDeAluguel = new BotaoPadrao(new ImageIcon("src/administrador/login-de-usuario.png"), 1151, 135, 28, 28);
		botaoAdicionarPropostaDeAluguel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent botaoAdicionarListaDePrecoPressionado) {
				dispose();
				new TelaCadastroPropostaDeAluguel();
			}
		});
		painelPropostasDeAluguel.add(botaoAdicionarPropostaDeAluguel);
		
		desenharTabela();

		BotaoPadrao botaoDetalharPropostaSelecionada = new BotaoPadrao("Detalhar proposta selecionada", PaletaDeCores.amarelo, PaletaDeCores.azulForte, 780, 439, 400);
		botaoDetalharPropostaSelecionada.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent botaoDetalharPropostaSelecionada) {
				int indiceLinhaSelecionada = tabelaPropostasDeAluguel.getSelectedRow();

				if (indiceLinhaSelecionada < 0) {
					JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada!", "Atenção", JOptionPane.WARNING_MESSAGE);
	        		return;
				}
				long id = Long.parseLong(String.valueOf(tabelaPropostasDeAluguel.getValueAt(indiceLinhaSelecionada, 0)));

				Sessao.setPropostaDeAluguelAtualParaDetalhamento(daoPropostaDeAluguel.buscarPorID(id));

				dispose();

				new TelaDetalhamentoPropostaPropostaDeAluguel();
			}			
		});
		painelPropostasDeAluguel.add(botaoDetalharPropostaSelecionada);

		desenharPainelFiltro();
		
		add(painelPropostasDeAluguel);
	}
	
	public void desenharTabela() {
		modeloDeDados = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) { return false; }
		};

		modeloDeDados.addColumn("ID");
		modeloDeDados.addColumn("CPF do locatário");
		modeloDeDados.addColumn("Nome do locatário");
		modeloDeDados.addColumn("Nome da peça");
		modeloDeDados.addColumn("Status");
		modeloDeDados.addColumn("Ingresso");

		daoPropostaDeAluguel = Sessao.getDaoPropostaDeAluguel();

		preencherDadosDaTabela(daoPropostaDeAluguel.buscarTodos());

		tabelaPropostasDeAluguel = new TabelaPadrao(modeloDeDados);

		int larguraScroll = 1160; 

		tabelaPropostasDeAluguel.getColumnModel().getColumn(0).setPreferredWidth((int)(larguraScroll * 0.12));
		tabelaPropostasDeAluguel.getColumnModel().getColumn(1).setPreferredWidth((int)(larguraScroll * 0.15));
		tabelaPropostasDeAluguel.getColumnModel().getColumn(2).setPreferredWidth((int)(larguraScroll * 0.30));
		tabelaPropostasDeAluguel.getColumnModel().getColumn(3).setPreferredWidth((int)(larguraScroll * 0.30));
		tabelaPropostasDeAluguel.getColumnModel().getColumn(4).setPreferredWidth((int)(larguraScroll * 0.15));
		tabelaPropostasDeAluguel.getColumnModel().getColumn(5).setPreferredWidth((int)(larguraScroll * 0.10));
		
		JScrollPane conteinerComScroll = new JScrollPane(tabelaPropostasDeAluguel);
		conteinerComScroll.setBounds(20, 173, larguraScroll, 243);
		painelPropostasDeAluguel.add(conteinerComScroll);
	}

	public void preencherDadosDaTabela(List<PropostaDeAluguel> propostasDeAluguel) {
		modeloDeDados.setNumRows(0);
		for (PropostaDeAluguel propostaDeAluguel : propostasDeAluguel) {
			Object[] linha = {propostaDeAluguel.getId(), propostaDeAluguel.getLocatario().getCpf(), propostaDeAluguel.getLocatario().getNome(), propostaDeAluguel.getPeca().getNome(), propostaDeAluguel.getStatus(), String.format("R$ %.2f", propostaDeAluguel.getPrecoTicket())};
			modeloDeDados.addRow(linha);
		}
	}

	private void desenharPainelFiltro() {
		Painel painelFiltro = new Painel(PaletaDeCores.branco, 20, 494, 570, 157);

		LabelPadrao labelStatus = new LabelPadrao("Status", 16, 23, 133, ValoresPadroes.alturaLabel);
		labelStatus.setHorizontalAlignment(LabelPadrao.RIGHT);
		painelFiltro.add(labelStatus);

		escolhaStatus = new ComboBoxPadrao<Status>(Status.values(), 167, 17, 227, ValoresPadroes.alturaCaixasDeTexto);
		painelFiltro.add(escolhaStatus);

		LabelPadrao labelNomeLocatario = new LabelPadrao("Nome do locatário", 16, 69, 133, ValoresPadroes.alturaLabel);
		labelNomeLocatario.setHorizontalAlignment(LabelPadrao.RIGHT);
		painelFiltro.add(labelNomeLocatario);

		caixaDeTextoNomeLocatario = new CaixaDeTextoPadrao(167, 63, 227);
		painelFiltro.add(caixaDeTextoNomeLocatario);

		LabelPadrao labelNomePeca = new LabelPadrao("Nome da peça", 16, 115, 133, ValoresPadroes.alturaLabel);
		labelNomePeca.setHorizontalAlignment(LabelPadrao.RIGHT);
		painelFiltro.add(labelNomePeca);

		caixaDeTextoNomePeca = new CaixaDeTextoPadrao(167, 109, 227);
		painelFiltro.add(caixaDeTextoNomePeca);

		BotaoPadrao botaoFiltrar = new BotaoPadrao(new ImageIcon("src/administrador/login-de-usuario.png"), 485, 17, 32, 32);
		botaoFiltrar.addActionListener(new OuvinteBotaoFiltrar(this));
		painelFiltro.add(botaoFiltrar);

		LabelPadrao labelFiltrar = new LabelPadrao("Filtrar", 449, 49, 103, ValoresPadroes.alturaLabel);
		labelFiltrar.setHorizontalAlignment(LabelPadrao.CENTER);
		painelFiltro.add(labelFiltrar);

		BotaoPadrao botaoLimparFiltro = new BotaoPadrao(new ImageIcon("src/administrador/login-de-usuario.png"), 485, 89, 32, 32);
		botaoLimparFiltro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent botaoLimparFiltroPressionado) {
				preencherDadosDaTabela(daoPropostaDeAluguel.buscarTodos());
			}
			
		});
		painelFiltro.add(botaoLimparFiltro);



		LabelPadrao labelLimparFiltro = new LabelPadrao("Limpar filtro", 449, 121, 103, ValoresPadroes.alturaLabel);
		labelLimparFiltro.setHorizontalAlignment(LabelPadrao.CENTER);
		painelFiltro.add(labelLimparFiltro);

		painelPropostasDeAluguel.add(painelFiltro);
	}
}

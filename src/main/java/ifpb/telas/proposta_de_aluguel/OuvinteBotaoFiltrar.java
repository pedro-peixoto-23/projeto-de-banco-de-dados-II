package ifpb.telas.proposta_de_aluguel;

import ifpb.enumeradores.Status;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.telas.Sessao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OuvinteBotaoFiltrar implements ActionListener {
    private final TelaPropostasDeAluguel tela;

    public OuvinteBotaoFiltrar(TelaPropostasDeAluguel tela) {
        this.tela = tela;
    }   

    public void actionPerformed(ActionEvent botaoFiltrarPressionado) {
        int statusIndice = tela.getEscolhaStatus().getSelectedIndex();
        String nomeLocatario = tela.getCaixaDeTextoNomeLocatario().getText();
        String nomePeca = tela.getCaixaDeTextoNomePeca().getText();

        if (statusIndice == 0 && nomeLocatario.isBlank() && nomePeca.isBlank()) {
            JOptionPane.showMessageDialog(null, "Preencha ao menos um campo para realizar o filtro!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        List<PropostaDeAluguel> todasAsPropostas = Sessao.getDaoPropostaDeAluguel().buscarTodos();
        List<PropostaDeAluguel> propostasEncontradas = new ArrayList<>();

        for (PropostaDeAluguel propostaDeAluguel : todasAsPropostas) {
            boolean statusValido = (statusIndice == 0) || (propostaDeAluguel.getStatus().equals(Status.values()[statusIndice]));
            boolean nomeLocatarioValido = (nomeLocatario.isBlank()) || (propostaDeAluguel.getLocatario().getNome().toLowerCase().contains(nomeLocatario.toLowerCase()));
            boolean nomePecaValido = (nomePeca.isBlank()) || (propostaDeAluguel.getPeca().getNome().toLowerCase().contains(nomePeca.toLowerCase()));

            if (statusValido && nomeLocatarioValido && nomePecaValido) {
                propostasEncontradas.add(propostaDeAluguel);
            }
        }

        tela.preencherDadosDaTabela(propostasEncontradas);
    }    
}

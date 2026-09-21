package ifpb.telas.proposta_de_aluguel;

import ifpb.email.Mensageiro;
import ifpb.excecoes.IntervaloDeDataInvalidoException;
import ifpb.excecoes.NovoIntervaloGerandoConflitoException;
import ifpb.modelo.intervalos.IntervaloDatas;
import ifpb.modelo.pessoa.Locatario;
import ifpb.modelo.proposta_de_aluguel.PeriodoExibicaoPeca;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.relatorios_ou_contratos.pdf.GeradorDeContratos;
import ifpb.telas.Sessao;
import org.apache.commons.mail2.core.EmailException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class OuvinteBotaoAplicarExtensao implements ActionListener {
    private final TelaEstenderContrato tela;

    public OuvinteBotaoAplicarExtensao(TelaEstenderContrato tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoAplicarExtensaoPressionado) {
        String textoDataInicio = tela.getCaixaDeTextoDataDeInicio().getText();
        String textoDataFim = tela.getCaixaTextoDataDeFim().getText();

        if (textoDataInicio.contains(" ") || textoDataFim.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Informe as duas datas!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        LocalDate dataInicio;
        LocalDate dataFim;

        try {
            DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

            dataInicio = LocalDate.parse(textoDataInicio, formatadorData);
            dataFim = LocalDate.parse(textoDataFim, formatadorData);
        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(null, "Data inválida!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        PropostaDeAluguel propostaDeAluguel = Sessao.getPropostaDeAluguelAtualParaDetalhamento();

        try {
            IntervaloDatas intervaloDatas = new IntervaloDatas(dataInicio, dataFim);

            PeriodoExibicaoPeca periodoReferencia = propostaDeAluguel.getPeriodosDeTempoExibicao().get(0);
            PeriodoExibicaoPeca novoPeriodo = new PeriodoExibicaoPeca(intervaloDatas, periodoReferencia.getTurno(), periodoReferencia.getIntervaloDeHorario(), periodoReferencia.getIntervaloDeOcupacaoDoTeatro(), Sessao.getDaoRegraDePreco().buscarTodos());

            Sessao.getDaoPropostaDeAluguel().verificarConflitoComPeriodosExistentes(novoPeriodo);

            propostaDeAluguel.adicionarNovoPeriodo(novoPeriodo);

            Sessao.getDaoPropostaDeAluguel().atualizar(propostaDeAluguel);

            Locatario locatario = propostaDeAluguel.getLocatario();

            GeradorDeContratos.gerarContrato(propostaDeAluguel);
            Mensageiro.enviarMensagem(locatario.getEmail(), "Contrato de aluguel", "Olá,\nSegue o seu contrato de aluguel atualizado com o novo período inserido.\n\nAtenciosamente,\nGerenciador de Teatro.", GeradorDeContratos.getNomeArquivoContrato());

            JOptionPane.showMessageDialog(null, "Contrato estendido com sucesso e cópia de contrato enviado para o email do locatário!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            tela.dispose();

            new TelaDetalhamentoPropostaPropostaDeAluguel();
        } catch (IntervaloDeDataInvalidoException exception) {
            JOptionPane.showMessageDialog(null, "O intervalo de datas é inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (NovoIntervaloGerandoConflitoException exception) {
            JOptionPane.showMessageDialog(null, "O novo período possui conflito com outro período!", "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (EmailException exception) {
            JOptionPane.showMessageDialog(null, "Não foi possível enviar o contrato por email!", "Atenção", JOptionPane.WARNING_MESSAGE);
            throw new RuntimeException(exception);
        }
    }
}
